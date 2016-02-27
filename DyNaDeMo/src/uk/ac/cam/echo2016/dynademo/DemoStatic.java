package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DemoStatic extends DemoObject {
    public DemoStatic(Spatial spatial, boolean isMainParent) {
        super(spatial, isMainParent);
        this.mass = 0;
    }
}
