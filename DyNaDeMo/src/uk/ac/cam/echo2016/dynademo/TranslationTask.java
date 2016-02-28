/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;

/**
 * @author tr393
 */
public class TranslationTask extends KinematicTask {
    public float averageSpeed;
    private Vector3f direction;
    private Vector3f endLocation;
    
    /**
     * 
     * @param taskQueueId - Id for the event time slot
     * @param completionTime
     * @param object
     * @param displacement
     * @param endLocation - Optional Vector3f specifying the final location.
     * Set as null if accuracy is not required
     */
    public TranslationTask(String taskQueueId, float completionTime, KinematicDemoObject object, Vector3f displacement, Vector3f endLocation) {
        super(taskQueueId, completionTime, object);
        this.direction = displacement;
        this.averageSpeed = displacement.length()/completionTime;
        this.direction = displacement.normalize(); // average speed is recorded first
        this.endLocation = endLocation;
    }
    
    @Override
    public void onTimeStep(float timePassed) {
        getObject().getSpatial().move(direction.mult(averageSpeed*timePassed));
    }
    
    /**
     * Extending classes should probably call this for accuracy.
     */
    @Override
    public void complete() {
        if (endLocation != null)
            getObject().getSpatial().setLocalTranslation(endLocation);
    }

    /**
     * @return the endLocation
     */
    public Vector3f getEndLocation() {
        return endLocation;
    }

    /**
     * @param endLocation the endLocation to set
     */
    public void setEndLocation(Vector3f endLocation) {
        this.endLocation = endLocation;
    }
}