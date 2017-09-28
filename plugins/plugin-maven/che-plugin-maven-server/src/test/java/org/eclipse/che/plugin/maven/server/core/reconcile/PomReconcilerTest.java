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
package org.eclipse.che.plugin.maven.server.core.reconcile;

import org.eclipse.che.plugin.maven.server.BaseTest;

/** @author Evgen Vidolob */
public class PomReconcilerTest extends BaseTest {

  //  private MavenProjectManager mavenProjectManager;
  //  private MavenWorkspace mavenWorkspace;
  //  private PomReconciler pomReconciler;
  //
  //  @BeforeMethod
  //  public void setUp() throws Exception {
  //    Provider<ProjectRegistry> projectRegistryProvider =
  //        (Provider<ProjectRegistry>) mock(Provider.class);
  //    when(projectRegistryProvider.get()).thenReturn(projectRegistry);
  //
  //    RequestTransmitter requestTransmitter = mock(RequestTransmitter.class);
  //
  //    MavenServerManagerTest.MyMavenServerProgressNotifier mavenNotifier =
  //        new MavenServerManagerTest.MyMavenServerProgressNotifier();
  //    MavenTerminal terminal =
  //        new MavenTerminal() {
  //          @Override
  //          public void print(int level, String message, Throwable throwable) throws RemoteException {
  //            System.out.println(message);
  //            if (throwable != null) {
  //              throwable.printStackTrace();
  //            }
  //          }
  //        };
  //
  //    File localRepository = new File(new File("target/localRepo").getAbsolutePath());
  //    localRepository.mkdirs();
  //    mavenServerManager.setLocalRepository(localRepository);
  //
  //    MavenWrapperManager wrapperManager = new MavenWrapperManager(mavenServerManager);
  //    mavenProjectManager =
  //        new MavenProjectManager(
  //            wrapperManager,
  //            mavenServerManager,
  //            terminal,
  //            mavenNotifier,
  //            new EclipseWorkspaceProvider());
  //    Provider<ProjectManager_> projectManagerProvider =
  //        (Provider<ProjectManager_>) mock(Provider.class);
  //    when(projectManagerProvider.get()).thenReturn(pm);
  //
  //    ClasspathManager classpathManager =
  //        new ClasspathManager(
  //            root.getAbsolutePath(), wrapperManager, mavenProjectManager, terminal, mavenNotifier);
  //
  //    mavenWorkspace =
  //        new MavenWorkspace(
  //            mavenProjectManager,
  //            mavenNotifier,
  //            new MavenExecutorService(),
  //            projectRegistryProvider,
  //            classpathManager,
  //            eventService,
  //            new EclipseWorkspaceProvider());
  //    EditorWorkingCopyManager editorWorkingCopyManager =
  //        new EditorWorkingCopyManager(
  //            projectManagerProvider,
  //            eventService,
  //            requestTransmitter,
  //            fileSystemManager,
  //            pathResolver,
  //            projectManager);
  //    pomReconciler =
  //        new PomReconciler(
  //            mavenProjectManager,
  //            editorWorkingCopyManager,
  //            eventService,
  //            mock(LanguageClient.class));
  //  }
  //
  //  @Test
  //  public void testProblemPosition() throws Exception {
  //    FolderEntry testProject = createTestProject("A", "");
  //    VirtualFileEntry child = testProject.getChild("pom.xml");
  //    String newContent = getPomContent("<ss");
  //    child.getVirtualFile().updateContent(newContent);
  //
  //    List<Problem> problems = pomReconciler.reconcile("/A/pom.xml", "/A", newContent);
  //    assertThat(problems).isNotEmpty();
  //    Problem problem = problems.get(0);
  //
  //    assertThat(problem.getSourceStart()).isEqualTo(newContent.indexOf("<ss") + 3);
  //    assertThat(problem.getSourceEnd()).isEqualTo(newContent.indexOf("<ss") + 4);
  //  }
  //
  //  @Test
  //  public void testReconcilePomWhenMavenProjectIsNotFound() throws Exception {
  //    FolderEntry testProject = createTestProject(PROJECT_NAME, "");
  //    VirtualFileEntry pom = testProject.getChild("pom.xml");
  //
  //    String projectPath = format("/%s/pom.xml", PROJECT_NAME);
  //    List<Problem> problems =
  //        pomReconciler.reconcile(
  //            projectPath + "/pom.xml", projectPath, pom.getVirtualFile().getContentAsString());
  //
  //    assertThat(problems).isEmpty();
  //    assertThat(pom).isNotNull();
  //  }
  //
  //  @Test
  //  public void testReconcilePomWhenPomContainsCorrectDependency() throws Exception {
  //    String dependency =
  //        "    <dependency>\n"
  //            + "        <groupId>junit</groupId>\n"
  //            + "        <artifactId>junit</artifactId>\n"
  //            + "        <version>3.8.1</version>\n"
  //            + "        <scope>test</scope>\n"
  //            + "    </dependency>\n";
  //    FolderEntry testProject =
  //        createTestProject(PROJECT_NAME, getPomContentWithDependency(dependency));
  //    VirtualFileEntry pom = testProject.getChild("pom.xml");
  //    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
  //    mavenWorkspace.update(Collections.singletonList(project));
  //    mavenWorkspace.waitForUpdate();
  //
  //    String projectPath = format("/%s/pom.xml", PROJECT_NAME);
  //    List<Problem> problems =
  //        pomReconciler.reconcile(
  //            projectPath + "/pom.xml", projectPath, pom.getVirtualFile().getContentAsString());
  //
  //    assertThat(problems).isEmpty();
  //    assertThat(pom).isNotNull();
  //  }
  //
  //  @Test
  //  public void testReconcilePomWhenPomContainsDependecyWithIncorrectVersion() throws Exception {
  //    String brokenDependency =
  //        "    <dependency>\n"
  //            + "        <groupId>junit</groupId>\n"
  //            + "        <artifactId>junit</artifactId>\n"
  //            + "        <version>33333333.8.1</version>\n"
  //            + "        <scope>test</scope>\n"
  //            + "    </dependency>\n";
  //    FolderEntry testProject =
  //        createTestProject(PROJECT_NAME, getPomContentWithDependency(brokenDependency));
  //    VirtualFileEntry pom = testProject.getChild("pom.xml");
  //    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
  //    mavenWorkspace.update(Collections.singletonList(project));
  //    mavenWorkspace.waitForUpdate();
  //
  //    String projectPath = "/" + PROJECT_NAME;
  //    List<Problem> problems =
  //        pomReconciler.reconcile(
  //            projectPath + "/pom.xml", projectPath, pom.getVirtualFile().getContentAsString());
  //
  //    assertThat(problems).hasSize(1);
  //    assertThat(problems.get(0).isError()).isTrue();
  //    assertThat(pom).isNotNull();
  //  }
  //
  //  @Test
  //  public void testReconcilePomWhenPomContainsDependecyWithIncorrectGroupId() throws Exception {
  //    String brokenDependency =
  //        "    <dependency>\n"
  //            + "        <groupId>junittttt</groupId>\n"
  //            + "        <artifactId>junit</artifactId>\n"
  //            + "        <version>3.8.1</version>\n"
  //            + "        <scope>test</scope>\n"
  //            + "    </dependency>\n";
  //    FolderEntry testProject =
  //        createTestProject(PROJECT_NAME, getPomContentWithDependency(brokenDependency));
  //    VirtualFileEntry pom = testProject.getChild("pom.xml");
  //    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
  //    mavenWorkspace.update(Collections.singletonList(project));
  //    mavenWorkspace.waitForUpdate();
  //
  //    String projectPath = "/" + PROJECT_NAME;
  //    List<Problem> problems =
  //        pomReconciler.reconcile(
  //            projectPath + "/pom.xml", projectPath, pom.getVirtualFile().getContentAsString());
  //
  //    assertThat(problems).hasSize(1);
  //    assertThat(problems.get(0).isError()).isTrue();
  //    assertThat(pom).isNotNull();
  //  }
  //
  //  @Test
  //  public void testReconcilePomWhenPomContainsDependecyWithIncorrectAtrifactId() throws Exception {
  //    String brokenDependency =
  //        "    <dependency>\n"
  //            + "        <groupId>junit</groupId>\n"
  //            + "        <artifactId>jjjjjjjunit</artifactId>\n"
  //            + "        <version>3.8.1</version>\n"
  //            + "        <scope>test</scope>\n"
  //            + "    </dependency>\n";
  //    FolderEntry testProject =
  //        createTestProject(PROJECT_NAME, getPomContentWithDependency(brokenDependency));
  //    VirtualFileEntry pom = testProject.getChild("pom.xml");
  //    IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
  //    mavenWorkspace.update(Collections.singletonList(project));
  //    mavenWorkspace.waitForUpdate();
  //
  //    String projectPath = "/" + PROJECT_NAME;
  //    List<Problem> problems =
  //        pomReconciler.reconcile(
  //            projectPath + "/pom.xml", projectPath, pom.getVirtualFile().getContentAsString());
  //
  //    assertThat(problems).hasSize(1);
  //    assertThat(problems.get(0).isError()).isTrue();
  //    assertThat(pom).isNotNull();
  //  }
  //
  //  private String getPomContentWithDependency(String dependency) {
  //    return format(
  //        "<groupId>org.eclipse.che.examples</groupId>\n"
  //            + "<artifactId>web-java-spring</artifactId>\n"
  //            + "<packaging>war</packaging>\n"
  //            + "<version>1.0-SNAPSHOT</version>\n"
  //            + "<name>SpringDemo</name>"
  //            + "<dependencies>\n"
  //            + "%s"
  //            + "</dependencies>",
  //        dependency);
  //  }
}
