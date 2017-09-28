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
package org.eclipse.che.api.fs.impl;

import static org.eclipse.che.api.fs.impl.FsConditionChecker.mustExist;
import static org.eclipse.che.api.fs.impl.FsConditionChecker.mustNotExist;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.api.PathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class DirectoryMover {

  private static final Logger LOG = LoggerFactory.getLogger(DirectoryMover.class);

  private final DirectoryCopier directoryCopier;
  private final DirectoryDeleter directoryDeleter;
  private final PathResolver pathResolver;

  @Inject
  public DirectoryMover(
      DirectoryCopier directoryCopier,
      DirectoryDeleter directoryDeleter,
      PathResolver pathResolver) {
    this.directoryCopier = directoryCopier;
    this.directoryDeleter = directoryDeleter;
    this.pathResolver = pathResolver;
  }

  public void move(String srcWsPath, String dstWsPath)
      throws NotFoundException, ConflictException, ServerException {
    Path srcFsPath = pathResolver.toFsPath(srcWsPath);
    Path dstFsPath = pathResolver.toFsPath(dstWsPath);

    mustExist(srcFsPath);
    mustExist(dstFsPath.getParent());
    mustNotExist(dstFsPath);

    try {
      FileUtils.moveDirectory(srcFsPath.toFile(), dstFsPath.toFile());
    } catch (IOException e) {
      String msg = "Failed to move directory: " + srcWsPath;
      LOG.error(msg, e);
      throw new ServerException(msg, e);
    }
  }

  public boolean moveQuietly(String srcWsPath, String dstWsPath) {
    Path srcFsPath = pathResolver.toFsPath(srcWsPath);
    Path dstFsPath = pathResolver.toFsPath(dstWsPath);

    if (!srcFsPath.toFile().exists()) {
      return false;
    }

    try {
      Files.createDirectories(dstFsPath.getParent());
      FileUtils.moveDirectory(srcFsPath.toFile(), dstFsPath.toFile());
      return true;
    } catch (IOException e) {
      LOG.error("Failed to move directory: {}", srcWsPath, e);
      return false;
    }
  }
}