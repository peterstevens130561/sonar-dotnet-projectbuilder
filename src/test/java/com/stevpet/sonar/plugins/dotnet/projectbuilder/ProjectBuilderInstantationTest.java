package com.stevpet.sonar.plugins.dotnet.projectbuilder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.picocontainer.DefaultPicoContainer;
import org.sonar.api.SonarPlugin;
import org.sonar.api.batch.bootstrap.ProjectBuilder;
import org.sonar.api.config.Settings;


public class ProjectBuilderInstantationTest {

	@Mock Settings settings ;
	DefaultPicoContainer picoContainer;
	
	@Before
	public void before() {
		org.mockito.MockitoAnnotations.initMocks(this);
		picoContainer = new DefaultPicoContainer() ;
		picoContainer.addComponent( settings);
		SonarPlugin plugin = new DotNetProjectBuilderPlugin();
		for(Object clazz : plugin.getExtensions()) {
			picoContainer.addComponent(clazz);
		}
		
	}
	
	@Test
	public void instantiate() {
		ProjectBuilder builder = picoContainer.getComponent(ProjectBuilder.class);		
	}
}
