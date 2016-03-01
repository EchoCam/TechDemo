/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

/**
 *
 * @author Tim
 */
public abstract class OnceConditionEvent extends ConditionEvent {
    OnceConditionEvent(String id) {
        super(id);
    }
    
    @Override
    public void onDemoEvent(MainApplication app) {
        app.getPollEventBus().remove(this);
        performAction(app);
    }
    
    public abstract void performAction(MainApplication app);
}
