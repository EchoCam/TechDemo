package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayDeque;

/**
 * @author tr393
 */
public class KinematicDemoObject extends DemoObject {
    private final ArrayDeque<DemoTask> tasks = new ArrayDeque<>();
    public KinematicDemoObject(String objId, Spatial spatial, float mass, boolean isMainParent) {
        super(objId, spatial, isMainParent);
        setMass(mass);
    }
    public void queueDisplacement(MainApplication app, float completionTime, Vector3f direction, float distance) {
        Vector3f start = getSpatial().getLocalTranslation();
        Vector3f end = getSpatial().getLocalTranslation().add(direction.normalize().mult(distance));
        tasks.add(new TranslationTask(getObjId(), completionTime, this, end.subtract(start)));
        app.addTask(getObjId(), tasks);
    }
    public void queueRotation(MainApplication app, float completionTime, Vector3f axis, float angle) {
        tasks.add(new RotationTask(getObjId(), completionTime, this, axis, angle));
        app.addTask(getObjId(), tasks);
    }
    public void queueDelay(MainApplication app, float completionTime) {
        tasks.add(new KinematicTask(getObjId(), completionTime, this));
        app.addTask(getObjId(), tasks);
    }
}