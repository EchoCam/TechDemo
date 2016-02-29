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
public class PossibleConditionalSyncPointEvent extends ConditionalSyncPointEvent {
    private ChoiceThenSyncPointEvent ctspe;
    private boolean trueIfTaken;
    
    public PossibleConditionalSyncPointEvent(String id, BoundingBox bound, String correctRoute, ChoiceThenSyncPointEvent ctspe, boolean trueIfTaken) {
        super(id, bound, correctRoute);
        this.ctspe = ctspe;
        this.trueIfTaken = trueIfTaken;
    }
    
    @Override
    public void onDemoEvent(MainApplication app) {
        if (app.getGameScreen().getRoute().equals(correctRoute)) {
            ctspe.setActionTaken(trueIfTaken);
            ctspe.onDemoEvent(app);
        }
    }
}
