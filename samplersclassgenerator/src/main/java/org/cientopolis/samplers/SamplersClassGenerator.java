package org.cientopolis.samplers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class SamplersClassGenerator {

    private String package_name;
    private String googleMaps_API_KEY;
    private List<StepClassGenerator> steps;
    private String mainHelpFileName;
    private static boolean authenticationEnabled = false;
    private static boolean authenticationOptional = true;

    public SamplersClassGenerator(String package_name, String manifest_path, String strings_path, String raw_path) {
        this.package_name = package_name;
        this.steps = new ArrayList<>();

        RawFilesManagement.setRawPath(raw_path);

        XMLManagement.setManifestFileName(manifest_path+"AndroidManifest.xml");
        XMLManagement.setStringsFileName(strings_path+"strings.xml");
    }


    public void setGoogleMaps_API_KEY(String googleMaps_API_KEY) {
        this.googleMaps_API_KEY = googleMaps_API_KEY;

        XMLManagement.addGoogleMapsAPIKey(this.googleMaps_API_KEY);
    }

    public void addStep(StepClassGenerator step) {
        steps.add(step);
    }

    public void setMainHelpFileName(String mainHelpFileName) {
        this.mainHelpFileName = mainHelpFileName;
    }

    public static void setAuthenticationEnabled(boolean authenticationEnabled) {
        SamplersClassGenerator.authenticationEnabled = authenticationEnabled;
    }

    public static void setAuthenticationOptional(boolean authenticationOptional) {
        SamplersClassGenerator.authenticationOptional = authenticationOptional;
    }

    public void generateMainActivity(String path, String activity_name, String welcomeMessage, NetworkConfiguration networkConfiguration) {

        List<String> output = new ArrayList<>();

        output.add("package " + package_name+";");
        output.add("");
        output.add("import android.os.Bundle;");
        output.add("import java.util.ArrayList;");
        output.add("import org.cientopolis.samplers.ui.SamplersMainActivity;");
        output.add("import org.cientopolis.samplers.network.NetworkConfiguration;");
        output.add("import org.cientopolis.samplers.framework.*;");
        output.add("import org.cientopolis.samplers.framework.information.*;");
        output.add("import org.cientopolis.samplers.framework.insertDate.*;");
        output.add("import org.cientopolis.samplers.framework.insertText.*;");
        output.add("import org.cientopolis.samplers.framework.insertTime.*;");
        output.add("import org.cientopolis.samplers.framework.location.*;");
        output.add("import org.cientopolis.samplers.framework.multipleSelect.*;");
        output.add("import org.cientopolis.samplers.framework.selectOne.*;");
        output.add("import org.cientopolis.samplers.framework.photo.*;");
        output.add("import org.cientopolis.samplers.framework.soundRecord.*;");
        output.add("import org.cientopolis.samplers.framework.route.*;");
        output.add("import org.cientopolis.samplers.authentication.AuthenticationManager;");


        output.add("");
        output.add("public class "+activity_name+" extends SamplersMainActivity {");
        output.add("");
        output.add("    @Override");
        output.add("    protected void onCreate(Bundle savedInstanceState) {");
        output.add("        super.onCreate(savedInstanceState);");

        // Set Network Configuration
        output.add("        NetworkConfiguration.setURL(\"" + networkConfiguration.URL + "\");");
        output.add("        NetworkConfiguration.setPARAM_NAME_SAMPLE(\"" + networkConfiguration.PARAM_NAME_SAMPLE + "\");");
        output.add("        NetworkConfiguration.setPARAM_NAME_USER_ID(\"" + networkConfiguration.PARAM_NAME_USER_ID + "\");");
        output.add("        NetworkConfiguration.setPARAM_NAME_AUTHENTICATION_TYPE(\"" + networkConfiguration.PARAM_NAME_AUTHENTICATION_TYPE + "\");");

        // Set the authentication configuration
        output.add("        AuthenticationManager.setAuthenticationEnabled(" + String.valueOf(authenticationEnabled) + ");");
        output.add("        AuthenticationManager.setAuthenticationOptional(" + String.valueOf(authenticationOptional) + ");");

        // Set Welcome Message ---------------------------------------------------------------------
        String varName = "welcomeMessage";
        XMLManagement.addString(varName, welcomeMessage);

        output.add("        String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("        lb_main_welcome_message.setText("+varName+");");

        output.add("    }");
        output.add("");

        // getWorkflow() ---------------------------------------------------------------------------
        output.add("    @Override");
        output.add("    protected Workflow getWorkflow() {");
        output.add("        Workflow workflow = new Workflow();");

        for (int i = 0; i < steps.size(); i++) {
            output.addAll(steps.get(i).generateStep(i,"workflow"));
        }

        output.add("        return workflow;");
        output.add("    }");

        // getMainHelpResourceId() Help File -------------------------------------------------------
        output.add("    @Override");
        output.add("    protected Integer getMainHelpResourceId() {");

        String returnValue = "null";
        if (this.mainHelpFileName != null) {
            String resourceFileName = RawFilesManagement.copyRawResourceFile(this.mainHelpFileName);
            if (resourceFileName != "") {
                returnValue = resourceFileName;
            }
        }

        output.add("        return "+returnValue+";");
        output.add("    }");
        // -----------------------------------------------------------------------------------------

        output.add("}");

        saveFile(output, path+activity_name+".java");

        // TODO Parametrize Label and Theme
        XMLManagement.addMainActivity(activity_name,"@string/app_name", "@style/SamplersFrameworkAppTheme");

    }

    private void saveFile(List<String> output, String filename) {

        try {
            File outFile = new File(filename);

            System.out.println(outFile.getCanonicalPath());

            BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));

            for (String line: output) {
                writer.write(line+"\n");
            }

            writer.close();

        } catch(Exception e) {
            e.printStackTrace();
        }

    }





}
