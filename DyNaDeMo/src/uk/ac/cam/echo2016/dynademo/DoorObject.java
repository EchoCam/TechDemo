package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DoorObject extends KinematicDemoObject {

    private boolean opening = false;

    public DoorObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, mass, isMainParent, bound);
    }

    public void open(MainApplication app) {
        if (!opening) {
            System.out.println("Opening");
            if (getTasks().isEmpty()) {
                queueDisplacement(app, 2f, Vector3f.UNIT_Y.negate(), 9f);
            } else {
                DemoTask currentTask = getTasks().getFirst();
                if (currentTask instanceof TranslationTask) {
                    getTasks().remove(currentTask);
                    
                    float remaining = currentTask.getRemainingTime();
                    float completion = currentTask.getCompletionTime();
                    float current = completion - remaining;
                    float x = (9f * current / completion);
                    System.out.println("Travel down by " + x);
                    queueDisplacement(app, current, Vector3f.UNIT_Y.negate(), x);
                } else if (currentTask instanceof AddPropertyTask) {
                    getTasks().remove(currentTask);
                }
            }
        }
        opening = true;
    }

    public void close(MainApplication app) {
        if (opening) {
            System.out.println("Closing");
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
                    System.out.println("Travel up by " + x);
                    queueDisplacement(app, current, Vector3f.UNIT_Y, x);
                } else if (currentTask instanceof AddPropertyTask) {
                    getTasks().remove(currentTask);
                }
            }
        }
        opening = false;
    }
}
