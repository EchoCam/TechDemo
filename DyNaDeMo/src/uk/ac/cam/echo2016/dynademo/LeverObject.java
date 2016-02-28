package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author tr393
 */
public class LeverObject extends KinematicDemoObject implements InteractableObject {
    private int leverCount = 0;
    
    public LeverObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound) {
        super(objId, spatial, mass, isMainParent, bound);
    }
    
    @Override
    public void interact(MainApplication app) {
        DemoRoute leverRoute = app.routes.get("LeverRoute");
        if (leverCount < 10) {
            if (leverCount % 2 == 0) {
                queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), -FastMath.PI / 2);
            } else {
                queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), FastMath.PI / 2);
            }
        } else {
            app.getGameScreen().setDialogueTextSequence(new String[]{"You broke it. Well done."});
        }
        ++leverCount;
        leverRoute.properties.putInt(getObjId(), leverCount);
    }
}