package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public abstract class DemoKinematicTask {
    private DemoKinematic object;
    private float completionTime;
    private float currentTime = 0;
    
    public DemoKinematicTask(DemoKinematic object, float completionTime) {
        this.object = object;
        this.completionTime = completionTime;
    }
    
    public abstract void update(float time);
    public abstract boolean isFinished();

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
}
