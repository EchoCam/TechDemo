/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 *
 * @author Tim
 */
public class ButtonObject extends KinematicDemoObject implements InteractableObject {

    public final static int DELAY = 1;
    private Initialiser.ChoiceThenSyncPointEvent cpe;
    public Vector3f displacement = new Vector3f(0f, 1f, 1f).normalize().mult(0.2f / (float) Math.sqrt(2f));

    public ButtonObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound, Initialiser.ChoiceThenSyncPointEvent cpe) {
        super(objId, spatial, mass, isMainParent, bound);
        this.cpe = cpe;
    }

    public void onPress() {
        cpe.setActionTaken(true);
        //FIXME
//        route.properties.putBoolean(getObjId(), true);
    }

    @Override
    public void interact(MainApplication app) {
        DemoRoute route = app.routes.get("ButtonRoute");

        // TODO different property/affect?
        route.properties.putBoolean(getObjId(), true);

        if (getTasks().isEmpty()) {
            onPress();
            getSpatial().move(displacement.negate());
            queueDelay(app, DELAY);
            queueDisplacement(app, 0.1f, displacement, displacement.length());
        }
    }
}
