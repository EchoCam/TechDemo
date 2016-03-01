/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;

/**
 *
 * @author Rjmcf
 */
public class ExitChoiceEvent extends ConditionalSyncPointEvent {
    private SyncAfterChoiceEvent ctspe;
    private boolean trueIfTaken;
    
    public ExitChoiceEvent(String id, boolean onceOnly, BoundingBox bound, String correctRoute, SyncAfterChoiceEvent ctspe, boolean trueIfTaken) {
        super(id, onceOnly, bound, correctRoute);
        this.ctspe = ctspe;
        this.trueIfTaken = trueIfTaken;
    }
    
    @Override
    public void performAction(MainApplication app) {
        if (app.getGameScreen().getRoute().equals(correctRoute)) {
            ctspe.setActionTaken(trueIfTaken);
            ctspe.performAction(app);
        }
    }
}
