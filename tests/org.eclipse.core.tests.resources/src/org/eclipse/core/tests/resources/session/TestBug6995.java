/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.core.tests.resources.session;

import java.util.Map;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.tests.harness.WorkspaceSessionTest;
import org.eclipse.core.tests.internal.builders.SortBuilder;
import org.eclipse.core.tests.internal.builders.TestBuilder;

/**
 * Tests the fix for bug 6995.  In this bug, a snapshot immediately after startup and
 * before doing any builds was losing the old built tree.  A subsequent build would 
 * revert to a full build.
 */
public class TestBug6995 extends WorkspaceSessionTest {
/**
 * Constructor for TestBug6995.
 */
public TestBug6995() {
	super();
}
/**
 * Constructor for TestBug6995.
 * @param name
 */
public TestBug6995(String name) {
	super(name);
}
/**
 * Create a project and configure a builder for it.
 */
public void test1() {
	//turn off autobuild
	IWorkspace workspace = getWorkspace();
	try {
		IWorkspaceDescription desc = workspace.getDescription();
		desc.setAutoBuilding(false);
		workspace.setDescription(desc);
	} catch (CoreException e) {
		fail("1.0", e);
	}
	
	//create a project and configure builder
	IProject project = workspace.getRoot().getProject("Project");
	try {
		project.create(getMonitor());
		project.open(getMonitor());
		
		IProjectDescription description = project.getDescription();
		ICommand command = description.newCommand();
		Map args = command.getArguments();
		args.put(TestBuilder.BUILD_ID, "Project1Build1");
		command.setBuilderName(SortBuilder.BUILDER_NAME);
		command.setArguments(args);
		description.setBuildSpec(new ICommand[] {command});
		project.setDescription(description, getMonitor());
	} catch (CoreException e) {
		fail("2.0", e);
	}
	
	//do an initial build
	try {
		project.build(IncrementalProjectBuilder.FULL_BUILD, getMonitor());
	} catch (CoreException e) {
		fail("3.0", e);
	}
	
	//save the workspace
	try {
		workspace.save(true, getMonitor());
	} catch (CoreException e) {
		fail("4.0", e);
	}
}
/**
 * After restarted the workspace, do a snapshot, then try to build.
 */
public void test2() {
	IWorkspace workspace = getWorkspace();
	IProject project = workspace.getRoot().getProject("Project");
	//snapshot
	try {
		workspace.save(false, getMonitor());
	} catch (CoreException e) {
		fail("1.0", e);
	}
	
	//build
	try {
		//make a change so build doesn't get short-circuited
		IFile file = project.getFile("File");
		file.create(getRandomContents(), true, getMonitor());
		project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, getMonitor());
	} catch (CoreException e) {
		fail("2.0", e);
	}
	
	//make sure an incremental build occurred
	SortBuilder builder = SortBuilder.getInstance();
	assertTrue("3.0", !builder.wasDeltaNull());
	assertTrue("3.1", builder.wasIncrementalBuild());
}
}