package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayDeque;

/**
 * @author tr393
 */
public class KinematicDemoObject extends DemoObject {
    private final String objId;
    private final ArrayDeque<DemoTask> tasks = new ArrayDeque<>();
    public KinematicDemoObject(String objId, Spatial spatial, float mass, boolean isMainParent) {
        super(spatial, isMainParent);
        this.objId = objId;
        this.mass = mass;
    }
    public void queueDisplacement(MainApplication app, float completionTime, Vector3f direction, float distance) {
        Vector3f start = spatial.getLocalTranslation();
        Vector3f end = spatial.getLocalTranslation().add(direction.normalize().mult(distance));
        tasks.add(new TranslationTask(objId, completionTime, this, end.subtract(start)));
        app.addTask(spatial.getName(), tasks);
    }
    public void queueRotation(MainApplication app, float completionTime, Vector3f axis, float angle) {
        tasks.add(new RotationTask(objId, completionTime, this, axis, angle));
        app.addTask(spatial.getName(), tasks);
    }
    public void queueDelay(MainApplication app, float completionTime) {
        tasks.add(new KinematicTask(objId, completionTime, this));
        app.addTask(spatial.getName(), tasks);
    }
}