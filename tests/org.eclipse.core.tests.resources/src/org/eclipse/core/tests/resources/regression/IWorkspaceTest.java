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
package org.eclipse.core.tests.resources.regression;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.internal.localstore.CoreFileSystemLibrary;
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.tests.harness.EclipseWorkspaceTest;
import junit.framework.*;

public class IWorkspaceTest extends EclipseWorkspaceTest {
public IWorkspaceTest() {
}
public IWorkspaceTest(String name) {
	super(name);
}
public static Test suite() {
	return new TestSuite(IWorkspaceTest.class);
}
protected void tearDown() throws Exception {
	super.tearDown();
	getWorkspace().getRoot().delete(true, null);
}
/**
 * 1GDKIHD: ITPCORE:WINNT - API - IWorkspace.move needs to keep history
 */
public void testMultiMove_1GDKIHD() {
	// create common objects
	IProject project = getWorkspace().getRoot().getProject("MyProject");
	try {
		project.create(getMonitor());
		project.open(getMonitor());
	} catch (CoreException e) {
		fail("0.0", e);
	}
	
	// test file (force = true)
	IFile file1 = project.getFile("file.txt");
	IFolder folder = project.getFolder("folder");
	IResource[] allResources = new IResource[] {file1, folder};
	try {
		folder.create(true, true, getMonitor());
		file1.create(getRandomContents(), true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().move(new IFile[] {file1}, folder.getFullPath(), true, getMonitor());
		file1.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file1.getHistory(getMonitor());
		assertEquals("1.0", 3, states.length);
		getWorkspace().delete(allResources, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("1.20", e);
	}

	// test file (force = false)
	try {
		folder.create(true, true, getMonitor());
		file1.create(getRandomContents(), true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().move(new IFile[] {file1}, folder.getFullPath(), false, getMonitor());
		file1.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file1.getHistory(getMonitor());
		assertEquals("2.0", 3, states.length);
		getWorkspace().delete(allResources, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("2.20", e);
	}

	// test folder (force = true)
	IFolder folder2 = project.getFolder("second folder");
	IFile file2 = folder.getFile("file2.txt");
	allResources = new IResource[] {file1, folder, folder2, file2};
	try {
		folder.create(true, true, getMonitor());
		folder2.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().move(new IResource[] {folder}, folder2.getFullPath(), true, getMonitor());
		folder.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file2.getHistory(getMonitor());
		assertEquals("3.0", 3, states.length);
		getWorkspace().delete(allResources, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("3.20", e);
	}

	// test folder (force = false)
	try {
		folder.create(true, true, getMonitor());
		folder2.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().move(new IResource[] {folder}, folder2.getFullPath(), false, getMonitor());
		folder.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file2.getHistory(getMonitor());
		assertEquals("4.0", 3, states.length);
		getWorkspace().delete(allResources, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("4.20", e);
	}
	
	try {
		project.delete(true, getMonitor());
	} catch (CoreException e) {
		fail("20.0", e);
	}
}
/**
 * 1GDGRIZ: ITPCORE:WINNT - API - IWorkspace.delete needs to keep history
 */
public void testMultiDelete_1GDGRIZ() {
	// create common objects
	IProject project = getWorkspace().getRoot().getProject("MyProject");
	try {
		project.create(getMonitor());
		project.open(getMonitor());
	} catch (CoreException e) {
		fail("0.0", e);
	}
	
	// test file (force = true)
	IFile file1 = project.getFile("file.txt");
	try {
		file1.create(getRandomContents(), true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().delete(new IFile[] {file1}, true, getMonitor());
		file1.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file1.getHistory(getMonitor());
		assertEquals("1.0", 3, states.length);
		getWorkspace().delete(new IResource[] {file1}, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("1.20", e);
	}

	// test file (force = false)
	try {
		file1.create(getRandomContents(), true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		file1.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().delete(new IFile[] {file1}, false, getMonitor());
		file1.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file1.getHistory(getMonitor());
		assertEquals("2.0", 3, states.length);
		getWorkspace().delete(new IResource[] {file1}, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("2.20", e);
	}

	// test folder (force = true)
	IFolder folder = project.getFolder("folder");
	IFile file2 = folder.getFile("file2.txt");
	try {
		folder.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().delete(new IResource[] {folder}, true, getMonitor());
		folder.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file2.getHistory(getMonitor());
		assertEquals("3.0", 3, states.length);
		getWorkspace().delete(new IResource[] {folder, file1, file2}, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("3.20", e);
	}

	// test folder (force = false)
	try {
		folder.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		file2.setContents(getRandomContents(), true, true, getMonitor());
		getWorkspace().delete(new IResource[] {folder}, false, getMonitor());
		folder.create(true, true, getMonitor());
		file2.create(getRandomContents(), true, getMonitor());
		IFileState[] states = file2.getHistory(getMonitor());
		assertEquals("4.0", 3, states.length);
		getWorkspace().delete(new IResource[] {folder, file1, file2}, true, getMonitor());
		project.clearHistory(getMonitor());
	} catch (CoreException e) {
		fail("4.20", e);
	}
	
	try {
		project.delete(true, getMonitor());
	} catch (CoreException e) {
		fail("20.0", e);
	}
}
public void test_8974() {
	IProject one = getWorkspace().getRoot().getProject("One");
	IPath oneLocation = getRandomLocation().append(one.getName());
	oneLocation.toFile().mkdirs();
	IProjectDescription oneDescription = getWorkspace().newProjectDescription(one.getName());
	oneDescription.setLocation(oneLocation);

	try {
		one.create(oneDescription, getMonitor());
	} catch (CoreException e) {
		Workspace.clear(oneLocation.removeLastSegments(1).toFile());
		fail("0.0", e);
	}

	try {
		IProject two = getWorkspace().getRoot().getProject("Two");
		IPath twoLocation = oneLocation.removeLastSegments(1).append(oneLocation.lastSegment().toLowerCase());
		
		IStatus result = getWorkspace().validateProjectLocation(two, twoLocation);
		if (CoreFileSystemLibrary.isCaseSensitive()) {
			assertTrue("1.0", result.isOK());
		} else {
			assertTrue("1.1", !result.isOK());
		}
		// cleanup
		ensureDoesNotExistInWorkspace(one);
	} finally {
		// ensure that the project directory is cleaned up.
		Workspace.clear(oneLocation.removeLastSegments(1).toFile());
	}
}
}