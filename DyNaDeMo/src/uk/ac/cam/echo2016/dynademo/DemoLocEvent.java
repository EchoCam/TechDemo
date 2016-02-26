package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;

/**
 * @author tr393
 */
public class DemoLocEvent extends DemoEvent {
    Vector3f trigLoc;
    int trigWidth; // x
    int trigHeight; // y
    int trigDepth; // z
    public DemoLocEvent(String id, Vector3f loc, int width, int height, int depth) {
        super(id);
        trigLoc = loc;
        trigWidth = width;
        trigHeight = height;
        trigDepth = depth;
    }
    public boolean checkCondition(Vector3f playerLoc) {
        return playerLoc.x>trigLoc.x && playerLoc.x<trigLoc.x+trigWidth
            && playerLoc.y>trigLoc.y && playerLoc.y<trigLoc.y+trigHeight
            && playerLoc.z>trigLoc.z && playerLoc.z<trigLoc.z+trigDepth;
    }
}
