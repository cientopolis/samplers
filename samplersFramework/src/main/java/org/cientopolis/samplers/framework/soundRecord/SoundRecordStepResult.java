package org.cientopolis.samplers.framework.soundRecord;

import org.cientopolis.samplers.framework.Step;
import org.cientopolis.samplers.framework.StepResult;
import org.cientopolis.samplers.framework.base.BaseStepResult;

/**
 * Created by laura on 14/09/17.
 */

public class SoundRecordStepResult extends BaseStepResult {

    private String soundFileName;

    public SoundRecordStepResult(Step step, String soundFileName) {
        super(step);
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
