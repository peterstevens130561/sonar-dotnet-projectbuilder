package com.stevpet.sonar.plugins.dotnet.resharper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.sonar.api.Properties;
import org.sonar.api.Property;
import org.sonar.api.PropertyType;
import org.sonar.api.SonarPlugin;

import com.stevpet.sonar.plugins.common.commandexecutor.DefaultProcessLock;
import com.stevpet.sonar.plugins.common.commandexecutor.LockedWindowsCommandLineExecutor;
import com.stevpet.sonar.plugins.dotnet.resharper.inspectcode.ReSharperCommandBuilder;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.DefaultInspectCodeResultsParser;
import com.stevpet.sonar.plugins.dotnet.resharper.issuesparser.DefaultIssueValidator;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.CSharpRegularReSharperProfileExporter;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.CSharpRegularReSharperProfileImporter;
import com.stevpet.sonar.plugins.dotnet.resharper.profiles.ReSharperSonarWayProfileCSharp;
import com.stevpet.sonar.plugins.dotnet.resharper.saver.DefaultInspectCodeIssuesSaver;
import com.stevpet.sonar.plugins.dotnet.utils.vstowrapper.implementation.SimpleMicrosoftWindowsEnvironment;
@Properties({
@Property(key = ReSharperConfiguration.MODE, defaultValue = "", name = "ReSharper activation mode", description = "Possible values : empty (means active), 'skip' and 'reuseReport'.", global = false, project = false, type = PropertyType.SINGLE_SELECT_LIST, options = {
        "skip", "reuseReport" }),

@Property(key = ReSharperConfiguration.INCLUDE_ALL_FILES, defaultValue = "true", name = "ReSharper file inclusion mode", description = "Determines if violations are reported on any file (ignores filters and unsupported file types) or only those supported by the dotNet core plugin.", global = false, project = false, type = PropertyType.BOOLEAN),
@Property(key = ReSharperConfiguration.CUSTOM_SEVERITIES_DEFINITON, defaultValue = "", name = "ReSharper custom severities", description = "Add &lt;String&gt; vales from ReSharper's custom definitions (including &lt:wpf:ResourceDictionary&gt;) A restart is required to take affect.", type = PropertyType.TEXT, global = true, project = false),
@Property(key = ReSharperConfiguration.PROFILE_NAME, defaultValue = ReSharperConfiguration.PROFILE_DEFAULT, name = "Profile", description = "Profile to which rules will be saved on restart, if profile does not exist", type = PropertyType.STRING, global = true, project = false),
@Property(key = ReSharperConfiguration.CUSTOM_SEVERITIES_PATH, name = "Path to custom severities settings", description = "Absolute path to file with exported ReSharper settings: RESHARPER, Manage Options...,Import/Export Settiings, Export to file,CodeInspection", type = PropertyType.STRING, global = true, project = false),

@Property(key=ReSharperConfiguration.BUILD_CONFIGURATION_KEY, name="Configuration",type=PropertyType.STRING,global=true,project=true,defaultValue=ReSharperConfiguration.BUILD_CONFIGURATIONS_DEFVALUE),
@Property(key=ReSharperConfiguration.BUILD_PLATFORM_KEY, name="Platform",type=PropertyType.STRING,global=true,project=true,defaultValue=ReSharperConfiguration.BUILD_PLATFORM_DEFVALUE)
})
public class ReSharperPlugin extends SonarPlugin {

    @Override
    public List getExtensions() {
        List imported=Arrays.asList();
        List exported=Arrays.asList(
                ReSharperConfiguration.class,
                SimpleMicrosoftWindowsEnvironment.class,
                CSharpRegularReSharperProfileExporter.class,
                CSharpRegularReSharperProfileImporter.class,
                ReSharperSonarWayProfileCSharp.class,
                ReSharperRuleRepositoryProvider.class, 
                ReSharperCommandBuilder.class,
                DefaultInspectCodeIssuesSaver.class,
                InspectCodeBatchData.class,
                DefaultInspectCodeRunner.class,
                DefaultInspectCodeResultsParser.class,
                DefaultIssueValidator.class,
                DefaultReSharperWorkflow.class,
                DefaultProcessLock.class,
                LockedWindowsCommandLineExecutor.class,
                ReSharperSensor.class);
        List extensions = new ArrayList();
        extensions.addAll(imported);
        extensions.addAll(exported);
        extensions.addAll(ReSharperConfiguration.getProperties());
        return extensions;
    }

}
