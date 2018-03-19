package org.cientopolis.samplers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Xavier on 02/06/2017.
 */

public class RouteStepClassGenerator extends BaseStepClassGenerator {

    private Integer nextStepId;
    private String textToShow;
    private Long interval;
    private Integer mapZoom;

    public RouteStepClassGenerator(int id, Integer nextStepId, String textToShow, Long interval, Integer mapZoom) {
        super(id);
        this.nextStepId = nextStepId;
        this.textToShow = textToShow;
        this.interval = interval;
        this.mapZoom = mapZoom;
    }


    @Override
    public List<String> generateStep(int stepIndex, String workflow_var) {

        List<String> output = new ArrayList<>();
        String varNameStep = "step"+ String.valueOf(stepIndex);

        String varName = "textToShowRoute"+ String.valueOf(stepIndex);

        XMLManagement.addString(varName, this.textToShow);

        output.add("    String "+varName +" = getResources().getString(R.string."+varName+"); ");
        output.add("    RouteStep "+varNameStep+" = new RouteStep("+String.valueOf(id)+","+varName+","+String.valueOf(nextStepId)+"); ");

        if (interval != null) {
            output.add("    "+varNameStep+".setInterval("+String.valueOf(interval)+");");
        }

        if (mapZoom != null) {
            output.add("    "+varNameStep+".setMapZoom("+String.valueOf(mapZoom)+");");
        }

        // Help file
        addHelpFile(output, varNameStep);

        output.add("    "+workflow_var+".addStep("+varNameStep+"); ");

        return output;
    }

}
