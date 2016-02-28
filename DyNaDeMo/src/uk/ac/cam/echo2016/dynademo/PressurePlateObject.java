package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public abstract class PressurePlateObject extends KinematicDemoObject {
    public final static int DELAY = 1;
    
    public PressurePlateObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, mass, isMainParent, bound);
    }

    public abstract void onPressed();
    public abstract void onRelease();

    public void activate(MainApplication app) {
        
        DemoRoute route = app.routes.get("PuzzleRoute");
        if (!(route.properties.containsKey(getObjId()))) {
            throw new RuntimeException("Error: Property not found.");
        }
        Boolean plateDown = route.properties.getBoolean(getObjId());
        // TODO again hacky like leverRod mesh
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
    }
    
}
