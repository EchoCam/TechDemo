package uk.ac.cam.echo2016.dynademo;

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

    public boolean checkCondition(Vector3f playerLoc) {
        
        return playerLoc.x > trigLoc.x && playerLoc.x < trigLoc.x + trigWidth && playerLoc.y > trigLoc.y
                && playerLoc.y < trigLoc.y + trigHeight && playerLoc.z > trigLoc.z
                && playerLoc.z < trigLoc.z + trigDepth;
    }
}
