package org.cientopolis.samplers;

import java.util.List;

/**
 * Created by Xavier on 08/10/2017.
 */

public abstract class BaseStepClassGenerator implements StepClassGenerator {

    protected int id;
    protected String helpFileName;

    public BaseStepClassGenerator(int id) {
        this.id = id;
    }

    @Override
    public void setHelpFile(String helpFileName) {
        this.helpFileName = helpFileName;
    }

    @Override
    public String getHelpFile() {
        return this.helpFileName;
    }

    protected void addHelpFile(List<String> output, String stepVarName) {

        if (this.getHelpFile() != null) {
            String resourceFileName = RawFilesManagement.copyRawResourceFile(this.getHelpFile());
            if (resourceFileName != "") {
                output.add("    "+stepVarName+".setHelpResourseId("+resourceFileName+"); ");
            }
        }

    }

}
