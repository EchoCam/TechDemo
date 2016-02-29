package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;

/**
 * @author tr393
 */
public class RotationTask extends KinematicTask {

    private float angularSpeed; // Rad/s
    private Vector3f axis;
    private Vector3f endRotation;

    /**
     *
     * @param taskQueueId - Id for the event time slot
     * @param completionTime
     * @param object
     * @param axis
     * @param angle
     * @param endRotation - Optional Vector3f specifying the final rotation. Set as null if accuracy is not required
     */
    public RotationTask(String taskQueueId, float completionTime, KinematicDemoObject object, Vector3f axis, float angle, Vector3f endRotation) {
        super(taskQueueId, completionTime, object);
        this.angularSpeed = angle / completionTime;
        this.axis = axis.normalize();
        this.endRotation = endRotation;
    }

    @Override
    public void onTimeStep(float timePassed) {
        float delta = angularSpeed * timePassed;
        getObject().getSpatial().rotate(axis.x * delta, axis.y * delta, axis.z * delta);
    }

    /**
     *
     * WARNING: Probably incorrect! Extending classes should probably call this for accuracy.
     */
    @Override
    public void onComplete() {
        if (endRotation != null) {
            getObject().getSpatial().setLocalRotation(new Quaternion().fromAngles(endRotation.x, endRotation.y, endRotation.z));
        }
    }

    /**
     * @return the endLocation
     */
    public Vector3f getEndRotation() {
        return endRotation;
    }

    /**
     * @param endLocation the endLocation to set
     */
    public void setEndRotation(Vector3f endRotation) {
        this.endRotation = endRotation;
    }
}
