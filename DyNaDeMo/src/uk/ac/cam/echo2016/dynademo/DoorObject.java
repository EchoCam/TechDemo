package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DoorObject extends KinematicDemoObject {
    public DoorObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, mass, isMainParent, bound);
    }
    
    public void open() {
        if (getTasks().isEmpty()) {
            
        }
    }
    public void close() {
        
    }
}




        if (!plateDown) {
            getSpatial().move(0, -0.75f, 0);
            route.properties.putBoolean(getObjId(), true);

            queueDelay(app, DELAY);
            queueDisplacement(app, 0.1f, Vector3f.UNIT_Y, 0.75f);
            queueProperty(app, 0.0f, route.properties, getObjId(), false);
            onPressed();
            app.addTask(new DemoTask(getObjId(), 0f){
                @Override
                public void complete() {
                    onRelease();
                }
            });
        }
        if (getTasks().isEmpty()) {
            throw new RuntimeException("Error: Illegal pressure plate state for: " + getObjId());
        } else {
            DemoTask currentTask = getTasks().getFirst();
            if (currentTask instanceof KinematicTask) {
                currentTask.resetTime();
            } else if (currentTask instanceof TranslationTask) {
                getTasks().remove(currentTask);
                float x = (-0.75f * currentTask.getCurrentTime() / currentTask.getCompletionTime());
                getSpatial().move(0, x, 0);
            } else if (currentTask instanceof AddPropertyTask) {
                getTasks().remove(currentTask);
            }
        }