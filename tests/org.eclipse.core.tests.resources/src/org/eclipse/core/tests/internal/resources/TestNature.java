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
package org.eclipse.core.tests.internal.resources;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 */
public class TestNature implements IProjectNature {
	private IProject project;
/**
 * Constructor for TestNature.
 */
public TestNature() {
	super();
}
/**
 * @see IProjectNature#configure()
 */
public void configure() throws CoreException {
}
/**
 * @see IProjectNature#deconfigure()
 */
public void deconfigure() throws CoreException {
}
/**
 * @see IProjectNature#getProject()
 */
public IProject getProject() {
	return project;
}
/**
 * @see IProjectNature#setProject(IProject)
 */
public void setProject(IProject project) {
	this.project = project;
}
}