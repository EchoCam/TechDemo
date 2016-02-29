package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * Subclass this class to create tasks for kinematic objects to perform over a specified amount of time. Instances of
 * this class simply perform a delay function (for padding tasks).
 *
 * @author tr393
 */
public class DemoTask {

    private final String taskQueueId;
    private final float completionTime;
    private float remainingTime;

    public DemoTask(String taskQueueId, float completionTime) {
        this.taskQueueId = taskQueueId;
        this.completionTime = completionTime;
        this.remainingTime = completionTime;
    }

    /**
     * Default implementation is a delay and this function does nothing.
     *
     * @param time
     */
    public void onTimeStep(float timePassed) {
    }

    /**
     * Extend this to specify what should happen at the end of the time period.
     */
    public void complete() {
    }

    public boolean isFinished() {
        return (remainingTime < 0);
    }

    public void updateTime(float timePassed) {
        remainingTime -= timePassed;
    }

    public void resetTime() {
        remainingTime = completionTime;
    }

    public void setCurrentTime(float time) {
        remainingTime = time;
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
     * @return the remainingTime
     */
    public float getRemainingTime() {
        return remainingTime;
    }
}
