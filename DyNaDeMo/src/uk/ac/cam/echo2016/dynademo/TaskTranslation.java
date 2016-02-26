/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;

/**
 * @author tr393
 */
public class TaskTranslation extends DemoKinematicTask {
    private float averageSpeed;
    private Vector3f direction;
    
    public TaskTranslation(DemoKinematic object, float completionTime, Vector3f start, Vector3f end) {
        super(object, completionTime);
        this.direction = end.subtract(start);
        this.averageSpeed = direction.length()/completionTime;
        this.direction = direction.normalize();
    }
    
    @Override
    public void update(float timePassed) {
        getObject().spatial.move(direction.mult(averageSpeed*timePassed));
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
