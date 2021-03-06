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
package org.eclipse.che.plugin.embedjsexample.ide.action;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.parts.PartStackType;
import org.eclipse.che.ide.api.parts.WorkspaceAgent;
import org.eclipse.che.plugin.embedjsexample.ide.view.HelloWorldViewPresenter;

/** Action for opening a part which embeds javascript code. */
@Singleton
public class HelloWorldAction extends Action {

  public static final String ACTION_ID = "helloWorldFromJSAction";

  private WorkspaceAgent workspaceAgent;
  private HelloWorldViewPresenter helloWorldViewPresenter;

  @Inject
  public HelloWorldAction(
      WorkspaceAgent workspaceAgent, HelloWorldViewPresenter helloWorldViewPresenter) {
    super("Show Hello World JavaScript View");
    this.workspaceAgent = workspaceAgent;
    this.helloWorldViewPresenter = helloWorldViewPresenter;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    workspaceAgent.openPart(helloWorldViewPresenter, PartStackType.INFORMATION);
    workspaceAgent.setActivePart(helloWorldViewPresenter);
  }
}
