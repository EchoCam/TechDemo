/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;

/**
 * @author tr393
 */
public class TranslationTask extends DemoKinematicTask {
    private float averageSpeed;
    private Vector3f displacement;
    
    public TranslationTask(DemoKinematic object, float completionTime, Vector3f displacement) {
        super(object, completionTime);
        this.displacement = displacement;
        this.averageSpeed = displacement.length()/completionTime;
        this.displacement = displacement.normalize();
    }
    
    @Override
    public void update(float timePassed) {
        getObject().spatial.move(displacement.mult(averageSpeed*timePassed));
        updateTime(timePassed);
    }
}
