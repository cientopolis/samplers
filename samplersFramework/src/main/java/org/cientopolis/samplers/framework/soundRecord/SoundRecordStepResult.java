package org.cientopolis.samplers.framework.soundRecord;

import org.cientopolis.samplers.framework.StepResult;

/**
 * Created by laura on 14/09/17.
 */

public class SoundRecordStepResult extends StepResult {

    private String soundFileName;

    public SoundRecordStepResult(int stepId, String soundFileName) {
        super(stepId);
        this.soundFileName = soundFileName;
    }

    public String getSoundFileName() {
        return soundFileName;
    }
}
