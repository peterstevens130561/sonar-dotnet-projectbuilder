package com.stevpet.sonar.plugins.dotnet.projectbuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.SonarPlugin;

public class ReSharperPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List exported=Arrays.asList(
                ProjectBuilderConfiguration.class);
        List extensions = new ArrayList();
        extensions.addAll(exported);
        return extensions;
    }

}
