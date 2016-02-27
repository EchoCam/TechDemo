package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DynamicDemoObject extends DemoObject {
    public DynamicDemoObject(Spatial spatial, float mass, boolean isMainParent) {
        super(spatial, isMainParent);
        this.mass = mass;
    }
}
