package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import com.jme3.math.Vector3f;

/**
 * When the player's center intersects with this class's bounding box, its action is performed
 * 
 * @author tr393
 */
public abstract class LocationEvent extends DemoEvent {
    Vector3f trigLoc;
    float trigWidth; // x
    float trigHeight; // y
    float trigDepth; // z

    /**
     * 
     * @param id - id of event
     * @param loc - lowest corner of bounding box
     * @param width - width of bounding box
     * @param height - height of bounding box
     * @param depth - depth of bounding box
     */
    public LocationEvent(String id, Vector3f loc, float width, float height, float depth) {
        super(id);
        trigLoc = loc;
        trigWidth = width;
        trigHeight = height;
        trigDepth = depth;
    }
    
    public void checkAndFireEvent(MainApplication app, Vector3f playerLoc) {
        if (overlapsThis(playerLoc)) onDemoEvent(app);
    }
    
    public boolean overlapsThis(Vector3f location) {
        return (location.x > trigLoc.x && location.x < trigLoc.x + trigWidth && location.y > trigLoc.y
                && location.y < trigLoc.y + trigHeight && location.z > trigLoc.z
                && location.z < trigLoc.z + trigDepth);
    }
}
