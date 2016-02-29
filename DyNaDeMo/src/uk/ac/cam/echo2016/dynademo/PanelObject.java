package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class PanelObject extends KinematicDemoObject {
    private boolean opening = false;
    private Vector3f openPos;
    private Vector3f closePos;

    public PanelObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound, Vector3f openPos, Vector3f closePos) {
        super(objId, spatial, mass, isMainParent, bound);
        this.openPos = openPos;
        this.closePos = closePos;
    }

    public void open(MainApplication app) {
        if (!opening) {
            Vector3f pos = getSpatial().getWorldTranslation();
            if (getTasks().isEmpty()) {
                Vector3f change = openPos.subtract(pos);
                queueDisplacement(app, change.length()/10f, change, change.length());
            } else {
                getTasks().clear();
                Vector3f change = openPos.subtract(pos);
                queueDisplacement(app, change.length()/10f, change, change.length());
//                DemoTask currentTask = getTasks().getFirst();
//                if (currentTask instanceof TranslationTask) {
//                    getTasks().remove(currentTask);
//                    float remaining = currentTask.getRemainingTime();
//                    float completion = currentTask.getCompletionTime();
//                    float current = completion - remaining;
//                    float x = (9f * current / completion);
//                    System.out.println("Travel down by " + x);
//                    queueDisplacement(app, current, Vector3f.UNIT_Y.negate(), x);
//                } else if (currentTask instanceof AddPropertyTask) {
//                    getTasks().remove(currentTask);
//                }
            }
        }
        opening = true;
    }

    public void close(MainApplication app) {
        if (opening) {
            Vector3f pos = getSpatial().getWorldTranslation();
            if (getTasks().isEmpty()) {
                Vector3f change = closePos.subtract(pos);
                queueDisplacement(app, change.length()/10f, change, change.length());
            } else {
                getTasks().clear();
                Vector3f change = closePos.subtract(pos);
                queueDisplacement(app, change.length()/10f, change, change.length());
            
            
            /*
            if (getTasks().isEmpty()) {
                queueDisplacement(app, 2f, Vector3f.UNIT_Y, 9f);
            } else {
                DemoTask currentTask = getTasks().getFirst();
                if (currentTask instanceof TranslationTask) {
                    getTasks().remove(currentTask);
                    float remaining = currentTask.getRemainingTime();
                    float completion = currentTask.getCompletionTime();
                    float current = completion - remaining;
                    float x = (9f * current / completion);
                    queueDisplacement(app, current, Vector3f.UNIT_Y, x);
                } else if (currentTask instanceof AddPropertyTask) {
                    getTasks().remove(currentTask);
                }*/
            }
        }
        opening = false;
    }
}
