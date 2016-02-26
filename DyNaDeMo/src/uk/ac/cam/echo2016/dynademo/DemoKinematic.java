package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public class DemoKinematic extends DemoObject {
    public DemoKinematic(Spatial spatial, float mass, boolean isMainParent) {
        super(spatial, isMainParent);
        this.mass = mass;
    }
}
