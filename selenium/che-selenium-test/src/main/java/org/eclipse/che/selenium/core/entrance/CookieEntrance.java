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
package org.eclipse.che.selenium.core.entrance;

import com.google.inject.Singleton;
import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.user.TestUser;
import org.openqa.selenium.Cookie;

/**
 * Enter the product by adding "session-access-key=[auth_token]" to the cookie of web driver.
 *
 * @author Dmytro Nochevnov
 */
@Singleton
public class CookieEntrance implements Entrance {

  /**
   * Login to product by using cookies.
   *
   * @param user
   * @param seleniumWebDriver
   */
  public void login(TestUser user, SeleniumWebDriver seleniumWebDriver) {
    Cookie accessKey = new Cookie("session-access-key", user.getAuthToken());
    seleniumWebDriver.manage().addCookie(accessKey);
  }
}
