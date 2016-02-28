
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;
import java.util.ArrayDeque;

/**
 * This subclass of DemoLocEvent also associates a {@code DemoObject} with it to use in its functions.
 * @author tr393
 */
public abstract class ProximityEvent extends LocationEvent {
    public DemoObject object;
    public ArrayDeque<DemoObject> activators = new ArrayDeque<>();
    
    public ProximityEvent(String id, Vector3f loc, float width, float height, float depth, DemoObject object) {
        super(id, loc, width, height, depth);
        this.object = object;
    }
    
    @Override
    public void checkAndFireEvent(MainApplication app, Vector3f playerLoc) {
        for(DemoObject activator : activators) {
            if (overlapsThis(activator.getSpatial().getWorldTranslation())) {
                onDemoEvent(app);
            }
        }
        if (overlapsThis(playerLoc) & app.getPlayerControl().onGround()) onDemoEvent(app);
    }
}
