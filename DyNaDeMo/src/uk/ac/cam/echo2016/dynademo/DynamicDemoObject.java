package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DynamicDemoObject extends DemoObject {
    public DynamicDemoObject(String objId, Spatial spatial, float mass, boolean isMainParent) {
        super(objId, spatial, isMainParent);
        setMass(mass);
    }
}
