/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.api.git;

import static com.google.common.collect.Sets.newConcurrentHashSet;
import static java.nio.file.Files.isDirectory;
import static java.util.Collections.singletonList;
import static org.eclipse.che.api.git.shared.FileChangedEventDto.Status.ADDED;
import static org.eclipse.che.api.git.shared.FileChangedEventDto.Status.MODIFIED;
import static org.eclipse.che.api.git.shared.FileChangedEventDto.Status.NOT_MODIFIED;
import static org.eclipse.che.api.git.shared.FileChangedEventDto.Status.UNTRACKED;
import static org.eclipse.che.api.vfs.watcher.FileWatcherManager.EMPTY_CONSUMER;
import static org.eclipse.che.dto.server.DtoFactory.newDto;
import static org.slf4j.LoggerFactory.getLogger;

import java.nio.file.PathMatcher;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Provider;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.core.jsonrpc.commons.RequestHandlerConfigurator;
import org.eclipse.che.api.core.jsonrpc.commons.RequestTransmitter;
import org.eclipse.che.api.git.shared.FileChangedEventDto;
import org.eclipse.che.api.git.shared.Status;
import org.eclipse.che.api.project.server.ProjectManager;
import org.eclipse.che.api.vfs.watcher.FileWatcherManager;
import org.slf4j.Logger;

/**
 * Detects changes in files and sends message to client Git handler.
 *
 * @author Igor Vinokur
 */
public class GitChangesDetector {
  private static final Logger LOG = getLogger(GitChangesDetector.class);

  private static final String GIT_DIR = ".git";
  private static final String INCOMING_METHOD = "track/git-change";
  private static final String OUTGOING_METHOD = "event/git-change";

  private final RequestTransmitter transmitter;
  private final FileWatcherManager manager;
  private final Provider<ProjectManager> projectManagerProvider;
  private final GitConnectionFactory gitConnectionFactory;

  private final Set<String> endpointIds = newConcurrentHashSet();

  private int id;

  @Inject
  public GitChangesDetector(
      RequestTransmitter transmitter,
      FileWatcherManager manager,
      Provider<ProjectManager> projectManagerProvider,
      GitConnectionFactory gitConnectionFactory) {
    this.transmitter = transmitter;
    this.manager = manager;
    this.projectManagerProvider = projectManagerProvider;
    this.gitConnectionFactory = gitConnectionFactory;
  }

  @Inject
  public void configureHandler(RequestHandlerConfigurator configurator) {
    configurator
        .newConfiguration()
        .methodName(INCOMING_METHOD)
        .noParams()
        .noResult()
        .withConsumer(endpointIds::add);
  }

  @PostConstruct
  public void startWatcher() {
    id = manager.registerByMatcher(matcher(), createConsumer(), modifyConsumer(), deleteConsumer());
  }

  @PreDestroy
  public void stopWatcher() {
    manager.unRegisterByMatcher(id);
  }

  private PathMatcher matcher() {
    return it ->
        !(isDirectory(it) || GIT_DIR.equals(it.getNameCount() > 2 ? it.getName(2).toString() : ""));
  }

  private Consumer<String> createConsumer() {
    return fsEventConsumer();
  }

  private Consumer<String> modifyConsumer() {
    return fsEventConsumer();
  }

  private Consumer<String> deleteConsumer() {
    return EMPTY_CONSUMER;
  }

  private Consumer<String> fsEventConsumer() {
    return it -> endpointIds.forEach(transmitConsumer(it));
  }

  private Consumer<String> transmitConsumer(String path) {
    return id -> {
      try {
        String normalizedPath = path.startsWith("/") ? path.substring(1) : path;
        String itemPath = normalizedPath.substring(normalizedPath.indexOf("/") + 1);
        String projectPath =
            projectManagerProvider
                .get()
                .getProject(normalizedPath.split("/")[0])
                .getBaseFolder()
                .getVirtualFile()
                .toIoFile()
                .getAbsolutePath();
        GitConnection connection = gitConnectionFactory.getConnection(projectPath);
        Status status = connection.status(singletonList(itemPath));
        FileChangedEventDto.Status fileStatus;
        if (status.getAdded().contains(itemPath)) {
          fileStatus = ADDED;
        } else if (status.getUntracked().contains(itemPath)) {
          fileStatus = UNTRACKED;
        } else if (status.getModified().contains(itemPath)
            || status.getChanged().contains(itemPath)) {
          fileStatus = MODIFIED;
        } else {
          fileStatus = NOT_MODIFIED;
        }

        transmitter
            .newRequest()
            .endpointId(id)
            .methodName(OUTGOING_METHOD)
            .paramsAsDto(
                newDto(FileChangedEventDto.class)
                    .withPath(path)
                    .withStatus(fileStatus)
                    .withEditedRegions(
                        gitConnectionFactory.getConnection(projectPath).getEditedRegions(itemPath)))
            .sendAndSkipResult();
      } catch (NotFoundException | ServerException e) {
        String errorMessage = e.getMessage();
        if (!("Not a git repository".equals(errorMessage))) {
          LOG.error(errorMessage);
        }
      }
    };
  }
}
