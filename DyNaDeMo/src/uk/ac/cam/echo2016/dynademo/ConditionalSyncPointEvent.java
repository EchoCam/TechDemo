/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;

 class ConditionalSyncPointEvent extends SyncPointEvent {

    protected String correctRoute;

    public ConditionalSyncPointEvent(String id, boolean onceOnly, BoundingBox bound, String theCorrectRoute) {
        super(id, onceOnly, bound);
        correctRoute = theCorrectRoute;
    }

    @Override
    public void performAction(MainApplication app) {
        if (app.getGameScreen().getRoute().equals(correctRoute)) {
            super.performAction(app);
        }
    }
}

