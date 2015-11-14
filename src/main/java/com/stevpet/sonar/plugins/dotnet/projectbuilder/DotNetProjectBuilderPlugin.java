package com.stevpet.sonar.plugins.dotnet.projectbuilder;

import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

public class DotNetProjectBuilderPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List exported=Arrays.asList(
                VisualStudioProjectBuilder.class);
   
        return exported;
    }

}
