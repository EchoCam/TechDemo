package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;

public class SyncPointEvent extends LocationEvent {

    public SyncPointEvent(String id, BoundingBox bound) {
        super(id, bound);
    }

    @Override
    public void onDemoEvent(MainApplication app) {
        app.execSyncPoint();
    }
}