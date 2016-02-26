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
    private Vector3f start;
    private Vector3f end;
    
    public TaskTranslation(DemoKinematic object, float completionTime, Vector3f start, Vector3f end) {
        super(object, completionTime);
        this.start = start;
        this.end = end;
    }
    
    @Override
    public boolean isFinished() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float time) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
