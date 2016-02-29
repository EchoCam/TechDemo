package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;

public abstract class ExitEvent extends LocationEvent {
    
    public ExitEvent(String id, BoundingBox bound) {
        super(id, bound);
        this.bound = bound;
    }
    public void onLeave() {}
}
