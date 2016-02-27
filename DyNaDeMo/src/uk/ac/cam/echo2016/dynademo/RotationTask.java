/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;

/**
 * @author tr393
 */
public class RotationTask extends DemoKinematicTask {
    private float angularSpeed; // Rad/s
    private Vector3f axis;
    
    public RotationTask(DemoKinematic object, float completionTime, Vector3f axis, float angle) {
        super(object, completionTime);
        this.angularSpeed = angle/completionTime;
        this.axis = axis.normalize();
    }
    @Override
    public void update(float timePassed) {
        float delta = angularSpeed*timePassed;
        getObject().spatial.rotate(axis.x*delta, axis.y*delta, axis.z*delta);
        updateTime(timePassed);
    }
}
