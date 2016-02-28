package uk.ac.cam.echo2016.dynademo;

import android.os.BaseBundle;
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
        getTasks().add(new TranslationTask(getObjId(), completionTime, this, end.subtract(start), end));
        app.addTask(getObjId(), getTasks());
    }
    public void queueRotation(MainApplication app, float completionTime, Vector3f axis, float angle) {
        Vector3f end = axis.normalize().mult(angle/2);
        getTasks().add(new RotationTask(getObjId(), completionTime, this, axis, angle, null));
        app.addTask(getObjId(), getTasks());
    }
    public void queueDelay(MainApplication app, float completionTime) {
        getTasks().add(new KinematicTask(getObjId(), completionTime, this));
        app.addTask(getObjId(), getTasks());
    }
    public void queueProperty(MainApplication app, float completionTime, BaseBundle properties, String key, Object property) {
        getTasks().add(new AddPropertyTask(getObjId(), completionTime, properties, key, property));
        app.addTask(getObjId(), getTasks());
    }

    /**
     * @return the tasks
     */
    public ArrayDeque<DemoTask> getTasks() {
        return tasks;
    }
}