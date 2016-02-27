package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * Subclass this class to create tasks for kinematic objects to perform over a specified amount of time.
 * Instances of this class simply perform a delay function (for padding tasks).
 * 
 * @author tr393
 */
public class DemoTask {
    private final String taskQueueId;
    private final float completionTime;
    private float currentTime;
    
    public DemoTask(String taskQueueId, float completionTime) {
        this.taskQueueId = taskQueueId;
        this.completionTime = completionTime;
        this.currentTime = completionTime;
    }
    
    /**
     * Default implementation is a delay and this function does nothing.
     * @param time 
     */
    public void update(float timePassed) {}
    public void complete() {}
    
    public boolean isFinished() {
        return (currentTime<0);
    }

    public String getTaskQueueId() {
        return taskQueueId;
    }

    /**
     * @return the completionTime
     */
    public float getCompletionTime() {
        return completionTime;
    }

    /**
     * @return the currentTime
     */
    public float getCurrentTime() {
        return currentTime;
    }
    
    public void updateTime(float timePassed) {
        currentTime -= timePassed;
    }
}
