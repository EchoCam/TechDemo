package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public abstract class DemoKinematicTask {
    private DemoKinematic object;
    private final float completionTime;
    private float currentTime;
    
    public DemoKinematicTask(DemoKinematic object, float completionTime) {
        this.object = object;
        this.completionTime = completionTime;
        this.currentTime = completionTime;
    }
    
    public abstract void update(float time);
    public boolean isFinished() {
        return (currentTime<0);
    }

    /**
     * @return the object
     */
    public DemoKinematic getObject() {
        return object;
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
