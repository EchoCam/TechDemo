package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Vector3f;
import java.util.ArrayDeque;

/**
 * This subclass of DemoLocEvent also associates a {@code DemoObject} with it to use in its functions.
 *
 * @author tr393
 */
public abstract class ProximityEvent extends LocationEvent {

    public DemoObject object;
    public ArrayDeque<DemoObject> activators = new ArrayDeque<>();

    public ProximityEvent(String id, boolean onceOnly, BoundingBox bound, DemoObject object) {
        super(id, onceOnly, bound);
        this.object = object;
    }
    
    @Override
    public boolean checkCondition(MainApplication app) {
        for (DemoObject activator : activators) {
            if (bound.intersects(activator.getBound())) return true;
        }
        BoundingSphere playerBound = new BoundingSphere(MainApplication.HALFCHARHEIGHT, app.getPlayerControl().getPhysicsLocation());
        return bound.intersects(playerBound);
    }
}
