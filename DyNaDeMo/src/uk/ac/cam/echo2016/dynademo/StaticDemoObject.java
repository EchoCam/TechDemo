package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class StaticDemoObject extends DemoObject {
    public StaticDemoObject(String objId, Spatial spatial, boolean isMainParent) {
        super(objId, spatial, isMainParent, null, null);
        setMass(0);
    }
}
