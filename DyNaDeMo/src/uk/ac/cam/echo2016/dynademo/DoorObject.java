package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DoorObject extends KinematicDemoObject {
    public DoorObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, mass, isMainParent, bound);
    }
}