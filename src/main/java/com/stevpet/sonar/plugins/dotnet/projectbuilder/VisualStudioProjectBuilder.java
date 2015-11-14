/*******************************************************************************
 *
 * SonarQube MsCover Plugin
 * Copyright (C) 2015 SonarSource
 * dev@sonar.codehaus.org
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 *
 * Author: Peter Stevens, peter@famstevens.eu
 *******************************************************************************/
package com.stevpet.sonar.plugins.dotnet.projectbuilder;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.SimpleVisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioAssemblyLocator;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.VisualStudioSolutionHierarchyHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.bootstrap.ProjectBuilder;
import org.sonar.api.batch.bootstrap.ProjectDefinition;
import org.sonar.api.config.Settings;

import java.util.List;

/**
 * ProjectBuilder for dotnet projects
 * 
 * The build method will be invoked by sonar in the ProjectBuild phase, and
 * populates the MicrosoftWindowsEnvironment
 * 
 * @author stevpet
 * 
 */
public class VisualStudioProjectBuilder extends ProjectBuilder {

	private static final Logger LOG = LoggerFactory
			.getLogger(VisualStudioProjectBuilder.class);

	private ProjectDefinition sonarRootProject;
	private VisualStudioModuleBuilder moduleBuilder;
	private VisualStudioSolutionHierarchyHelper hierarchyHelper ;

	public VisualStudioProjectBuilder( Settings settings) {
		this(new VisualStudioModuleBuilder(),new VisualStudioSolutionHierarchyHelper(settings,new VisualStudioAssemblyLocator(settings)));
	}

	private VisualStudioProjectBuilder( VisualStudioModuleBuilder moduleBuilder,
			VisualStudioSolutionHierarchyHelper hierarchyHelper) {
		this.moduleBuilder = moduleBuilder;
		this.hierarchyHelper= hierarchyHelper ;
	}


	@Override
	public void build(Context context) {
		sonarRootProject = context.projectReactor().getRoot();
		LOG.info("Building project structure for {} ", sonarRootProject.getName());
		moduleBuilder.setRoot(sonarRootProject);
		hierarchyHelper.build(sonarRootProject.getBaseDir());
		List<SimpleVisualStudioProject> projects=hierarchyHelper.getProjects();
		
		for (SimpleVisualStudioProject project : projects) {
			if (!moduleBuilder.contains(project)) {
				moduleBuilder.add(project);
			}
		}
		moduleBuilder.build();
	}
}