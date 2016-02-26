/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

/**
 * @author tr393
 */
public class TaskRotation extends DemoKinematicTask {
    private float startAngle;
    private float endAngle;
    
    public TaskRotation(DemoKinematic object, float completionTime, float startAngle, float endAngle) {
        super(object, completionTime);
        this.startAngle = startAngle;
        this.endAngle = endAngle;
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
