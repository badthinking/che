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
package org.eclipse.che.plugin.php.projecttype;

import com.google.inject.Inject;
import java.io.InputStream;
import java.util.Map;
import org.eclipse.che.api.core.ConflictException;
import org.eclipse.che.api.core.ForbiddenException;
import org.eclipse.che.api.core.NotFoundException;
import org.eclipse.che.api.core.ServerException;
import org.eclipse.che.api.fs.api.FsManager;
import org.eclipse.che.api.fs.api.PathResolver;
import org.eclipse.che.api.project.server.handlers.CreateProjectHandler;
import org.eclipse.che.api.project.server.type.AttributeValue;
import org.eclipse.che.plugin.php.shared.Constants;

public class PhpProjectGenerator implements CreateProjectHandler {

  private final FsManager fsManager;
  private final PathResolver pathResolver;

  @Inject
  public PhpProjectGenerator(FsManager fsManager, PathResolver pathResolver) {
    this.fsManager = fsManager;
    this.pathResolver = pathResolver;
  }

  @Override
  public void onCreateProject(
      String projectWsPath, Map<String, AttributeValue> attributes, Map<String, String> options)
      throws ForbiddenException, ConflictException, ServerException, NotFoundException {
    fsManager.createDirectory(projectWsPath);
    InputStream inputStream =
        getClass().getClassLoader().getResourceAsStream("files/default_php_content");
    String wsPath = pathResolver.resolve(projectWsPath, "hello.php");
    fsManager.createFile(wsPath, inputStream);
  }

  @Override
  public String getProjectType() {
    return Constants.PHP_PROJECT_TYPE_ID;
  }
}
