
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;

/**
 * This subclass of DemoLocEvent also associates a {@code DemoObject} with it to use in its functions.
 * @author tr393
 */
public abstract class DemoProximityEvent extends DemoLocEvent {
    public DemoObject object;
    
    public DemoProximityEvent(String id, Vector3f loc, float width, float height, float depth, DemoObject object) {
        super(id, loc, width, height, depth);
        this.object = object;
    }
}
