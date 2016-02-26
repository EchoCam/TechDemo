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
    public void queueTranslation(MainApplication app, Vector3f direction, float distance, float completionTime) {
        Vector3f start = spatial.getLocalTranslation();
        Vector3f end = spatial.getLocalTranslation().add(direction.normalize().mult(distance));
        tasks.add(new TaskTranslation(this, completionTime, start, end));
        app.addTask(this, tasks);
    }
}
