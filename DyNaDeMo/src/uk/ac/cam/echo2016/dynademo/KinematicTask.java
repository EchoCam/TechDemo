package uk.ac.cam.echo2016.dynademo;


/**
 * Subclass this class to create tasks for kinematic objects to perform over a specified amount of time.
 * Instances of this class simply perform a delay function (for padding tasks).
 * 
 * @author tr393
 */
public class KinematicTask extends DemoTask {
    
    private KinematicDemoObject object;
    
    public KinematicTask(String taskQueueId, float completionTime, KinematicDemoObject object) {
        super(taskQueueId, completionTime);
        this.object = object;
    }
    
    /**
     * @return the object
     */
    public KinematicDemoObject getObject() {
        return object;
    }
}
