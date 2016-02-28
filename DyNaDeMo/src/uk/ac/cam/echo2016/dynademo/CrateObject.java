package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public class CrateObject extends DynamicDemoObject implements InteractableObject {
    public CrateObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, mass, isMainParent, bound);
    }

    @Override
    public void interact(MainApplication app) {
        app.drag(getSpatial());
    }
}
