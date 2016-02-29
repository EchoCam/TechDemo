/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;

 class ConditionalSyncPointEvent extends SyncPointEvent {

    protected String correctRoute;

    public ConditionalSyncPointEvent(String id, BoundingBox bound, String theCorrectRoute) {
        super(id, bound);
        correctRoute = theCorrectRoute;
    }

    @Override
    public void onDemoEvent(MainApplication app) {
        if (app.getGameScreen().getRoute().equals(correctRoute)) {
            super.onDemoEvent(app);
        }
    }
}

