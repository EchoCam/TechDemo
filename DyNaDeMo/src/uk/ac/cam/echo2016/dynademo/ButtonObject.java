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
    private SyncAfterChoiceEvent cpe;
    public Vector3f displacement = new Vector3f(-1f, 1f, 0).normalize().mult(0.2f / (float) Math.sqrt(2f));
    private boolean activated = false;

    public ButtonObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound, SyncAfterChoiceEvent cpe) {
        super(objId, spatial, mass, isMainParent, bound);
        this.cpe = cpe;
    }

    public void onPress() {
        performAction();
        cpe.setActionTaken(true);
        if(!activated) {
            activated = true;
        }
    }
    public void performAction() {
    }
    
    @Override
    public void interact(MainApplication app) {
        
        DemoScene route = app.routes.get("ButtonRoute");

        // TODO different property/affect?
        route.properties.putBoolean(getObjId(), true);

        if (getTasks().isEmpty()) {
            onPress();
            getSpatial().move(displacement.negate());
            queueDelay(app, DELAY);
            queueDisplacement(app, 0.1f, displacement, displacement.length());
        }
    }

    /**
     * @return the activated
     */
    public boolean isActivated() {
        return activated;
    }
}
