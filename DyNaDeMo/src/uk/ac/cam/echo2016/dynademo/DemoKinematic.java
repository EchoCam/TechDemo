package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayDeque;

/**
 * @author tr393
 */
public class DemoKinematic extends DemoObject {
    private ArrayDeque<DemoKinematicTask> tasks = new ArrayDeque<>();
    public DemoKinematic(Spatial spatial, float mass, boolean isMainParent) {
        super(spatial, isMainParent);
        this.mass = mass;
    }
    public void queueTranslation(MainApplication app, float completionTime, Vector3f direction, float distance) {
        Vector3f start = spatial.getLocalTranslation();
        Vector3f end = spatial.getLocalTranslation().add(direction.normalize().mult(distance));
        tasks.add(new TranslationTask(this, completionTime, end.subtract(start)));
        app.addTask(this, tasks);
    }
    public void queueRotation(MainApplication app, float completionTime, Vector3f axis, float angle) {
        tasks.add(new RotationTask(this, completionTime, axis, angle));
        app.addTask(this, tasks);
    }
    public void queueDelay(MainApplication app, float completionTime) {
        tasks.add(new DemoKinematicTask(this, completionTime));
        app.addTask(this, tasks);
    }
}