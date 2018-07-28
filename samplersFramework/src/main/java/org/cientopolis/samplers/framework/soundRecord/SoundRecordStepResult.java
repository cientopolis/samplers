package org.cientopolis.samplers.framework.soundRecord;

import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by laura on 14/09/17.
 */

public class SoundRecordStepResult extends BaseStepResult {

    private String soundFileName;

    public SoundRecordStepResult(int stepId, String soundFileName) {
        super(stepId);
        this.soundFileName = soundFileName;
    }

    public String getSoundFileName() {
        return soundFileName;
    }

    @Override
    public boolean hasMultimediaFile() {
        return true;
    }

    @Override
    public String getMultimediaFileName() {
        return soundFileName;
    }
}
