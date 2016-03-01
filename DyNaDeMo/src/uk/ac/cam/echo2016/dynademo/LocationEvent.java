package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import com.jme3.bounding.BoundingSphere;
import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;

/**
 * When the player's center intersects with this class's bounding box, its action is performed
 *
 * @author tr393
 */
public abstract class LocationEvent extends ConditionEvent {

    BoundingBox bound;

    /**
     *
     * @param id - id of event
     * @param loc - lowest corner of bounding box
     * @param width - width of bounding box
     * @param height - height of bounding box
     * @param depth - depth of bounding box
     */
    public LocationEvent(String id, boolean onceOnly, BoundingBox bound) {
        super(id, onceOnly);
        this.bound = bound;
    }
    @Override
    public boolean checkCondition(MainApplication app) {
        // Capsule not supported by jMonkey :(
        BoundingSphere playerBound =
                new BoundingSphere(MainApplication.HALFCHARHEIGHT, app.getPlayerControl().getPhysicsLocation());
        return bound.intersects(playerBound);
    }
}
