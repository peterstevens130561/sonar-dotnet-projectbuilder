package com.stevpet.sonar.plugins.dotnet.projectbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.bootstrap.ProjectDefinition;

import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.VisualStudioProject;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.SimpleVisualStudioProject;

public class VisualStudioModuleBuilder {

    private final Logger LOG = LoggerFactory.getLogger(VisualStudioModuleBuilder.class);
    private ProjectDefinition sonarRootProject;
    private List<ProjectDefinition> childProjects = new ArrayList<>();

    public void setRoot(ProjectDefinition sonarRootProject) {
        this.sonarRootProject = sonarRootProject;
    }

    public boolean contains(VisualStudioProject project) {
        List<ProjectDefinition> subProjects = sonarRootProject.getSubProjects();
        String key = getKey(project);
        for(ProjectDefinition module:subProjects) {
            String moduleKey=module.getKey();
            if(key.equals(moduleKey)) {
                return true;
            }
        }
        
        return false;
    }
    /**
     * Prepare a module for addition
     * @param project
     */
    public void add(VisualStudioProject project) {

        ProjectDefinition newProject = ProjectDefinition.create();
        Properties properties = (Properties) sonarRootProject.getProperties().clone();
        newProject.setProperties(properties);
        
        String name = project.getAssemblyName();
        newProject.setName(name);
        
        String key = getKey(project);
        newProject.setKey(key); 

        String assembly = project.getArtifactFile().getAbsolutePath();
        newProject.setProperty("sonar.cs.fxcop.assembly", assembly);

        File projectDirectory = project.getDirectory();
        newProject.setBaseDir(projectDirectory);
        
        newProject.setWorkDir(new File(projectDirectory, ".sonar"));
        if(project.isUnitTest()) {
            newProject.resetTestDirs();
            newProject.resetSourceDirs();
            newProject.setTestDirs(projectDirectory);
        }
        newProject.setVersion(sonarRootProject.getVersion());
        LOG.debug(" - basedir {}",newProject.getBaseDir().getAbsolutePath());
        LOG.debug("  - Adding Sub Project => {}", newProject.getName());

        childProjects.add(newProject);
    }

    private String getKey(VisualStudioProject project) {
        String name=project.getAssemblyName();
        String key = sonarRootProject.getKey() + ":" + name.replaceAll(" ", "_");
        return key;
    }
    
    /**
     * add all modules to the root
     */
    void build() {
        for (ProjectDefinition childProject : childProjects) {
            sonarRootProject.addSubProject(childProject);
        }
    }
}
