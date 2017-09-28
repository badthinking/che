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
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.api.PathResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class FileMover {

  private static final Logger LOG = LoggerFactory.getLogger(FileMover.class);

  private final FileCopier fileCopier;
  private final FileDeleter fileDeleter;
  private final PathResolver pathResolver;

  @Inject
  public FileMover(FileCopier fileCopier, FileDeleter fileDeleter, PathResolver pathResolver) {
    this.fileCopier = fileCopier;
    this.fileDeleter = fileDeleter;
    this.pathResolver = pathResolver;
  }

  public void move(String srcWsPath, String dstWsPath)
      throws NotFoundException, ConflictException, ServerException {

    Path srcFsPath = pathResolver.toFsPath(srcWsPath);
    Path dstFsPath = pathResolver.toFsPath(dstWsPath);

    mustExist(srcFsPath);
    mustNotExist(dstFsPath);

    try {
      Files.move(srcFsPath, dstFsPath);
    } catch (IOException e) {
      String msg = "Failed to move file: " + srcWsPath;
      LOG.error(msg);
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

      Files.move(srcFsPath, dstFsPath);
      return true;
    } catch (IOException e) {
      LOG.error("Failed to move file: {}", srcWsPath);

      return false;
    }
  }
}