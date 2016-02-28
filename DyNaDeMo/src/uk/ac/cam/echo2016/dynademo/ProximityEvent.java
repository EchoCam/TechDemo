
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Vector3f;
import java.util.ArrayDeque;

/**
 * This subclass of DemoLocEvent also associates a {@code DemoObject} with it to use in its functions.
 * @author tr393
 */
public abstract class ProximityEvent extends LocationEvent {
    public DemoObject object;
    public ArrayDeque<DemoObject> activators = new ArrayDeque<>();
    
    public ProximityEvent(String id, BoundingBox bound, DemoObject object) {
        super(id, bound);
        this.object = object;
    }
    
    @Override
    public void checkAndFireEvent(MainApplication app, Vector3f playerLoc) {
        for(DemoObject activator : activators) {
            System.out.println(activator.getBound().getCenter());
            if (bound.intersects(activator.getBound())) {
                onDemoEvent(app);
            }
        }
        BoundingSphere playerBound = new BoundingSphere(MainApplication.HALFCHARHEIGHT, playerLoc);
        if (bound.intersects(playerBound)) 
            onDemoEvent(app);
    }
}
