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

import org.eclipse.che.selenium.core.SeleniumWebDriver;
import org.eclipse.che.selenium.core.user.TestUser;

/** @author Dmytro Nochevnov */
public interface Entrance {
  /**
   * Login to product.
   *
   * @param user user to login
   * @param seleniumWebDriver
   */
  void login(TestUser user, SeleniumWebDriver seleniumWebDriver);
}
