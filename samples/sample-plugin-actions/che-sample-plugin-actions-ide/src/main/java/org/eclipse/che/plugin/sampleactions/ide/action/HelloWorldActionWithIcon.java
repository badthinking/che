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
package org.eclipse.che.plugin.sampleactions.ide.action;

import javax.inject.Inject;
import javax.inject.Singleton;
import org.eclipse.che.ide.api.action.Action;
import org.eclipse.che.ide.api.action.ActionEvent;
import org.eclipse.che.ide.api.notification.NotificationManager;
import org.eclipse.che.ide.api.notification.StatusNotification;
import org.eclipse.che.plugin.sampleactions.ide.SampleActionsResources;

/** Action for showing a string via the {@link NotificationManager} */
@Singleton
public class HelloWorldActionWithIcon extends Action {

  private NotificationManager notificationManager;

  @Inject
  public HelloWorldActionWithIcon(NotificationManager notificationManager) {
    super(
        "Say Hello World", "Say Hello World Action", null, SampleActionsResources.INSTANCE.icon());
    this.notificationManager = notificationManager;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    this.notificationManager.notify(
        "Hello World with Icon",
        StatusNotification.Status.SUCCESS,
        StatusNotification.DisplayMode.FLOAT_MODE);
  }
}
