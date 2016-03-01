package uk.ac.cam.echo2016.dynademo;

import android.os.BaseBundle;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayDeque;

/**
 * @author tr393
 */
public class KinematicDemoObject extends DemoObject {

    private final ArrayDeque<DemoTask> tasks = new ArrayDeque<>();

    public KinematicDemoObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, isMainParent, bound);
        setMass(mass);
    }

    public void queueDisplacement(MainApplication app, float completionTime, Vector3f direction, float distance) {
        Vector3f start = getSpatial().getLocalTranslation();
        Vector3f end = getSpatial().getLocalTranslation().add(direction.normalize().mult(distance));
        DemoTask task = new TranslationTask(getObjId(), completionTime, this, end.subtract(start), end);
        tasks.add(task);
        app.addTaskQueue(getObjId(), tasks);
    }

    public void queueRotation(MainApplication app, float completionTime, Vector3f axis, float angle) {
        Vector3f end = axis.normalize().mult(angle);
        DemoTask task = new RotationTask(getObjId(), completionTime, this, axis, angle, null);
        tasks.add(task);
        app.addTaskQueue(getObjId(), tasks);
    }

    public void queueDelay(MainApplication app, float completionTime) {
        DemoTask task = new KinematicTask(getObjId(), completionTime, this);
        tasks.add(task);
        app.addTaskQueue(getObjId(), tasks);
    }

    public void queueProperty(MainApplication app, float completionTime, BaseBundle properties, String key, Object property) {
        DemoTask task = new AddPropertyTask(getObjId(), completionTime, properties, key, property);
        tasks.add(task);
        app.addTaskQueue(getObjId(), tasks);
    }

    /**
     * @return the tasks
     */
    public ArrayDeque<DemoTask> getTasks() {
        return tasks;
    }
}