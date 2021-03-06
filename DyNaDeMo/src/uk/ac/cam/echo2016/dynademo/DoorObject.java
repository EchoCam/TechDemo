package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DoorObject extends KinematicDemoObject implements InteractableObject{
    public final static float SPEED = 1f;
    private boolean open = false;
    private float openAngle;
    public boolean locked;

    public DoorObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound, float openAngle, boolean isLocked) {
        super(objId, spatial, mass, isMainParent, bound);
        this.openAngle = -openAngle;
        locked = isLocked;
    }

    @Override
    public void interact(MainApplication app) {
        if (locked) {
            app.getGameScreen().setDialogueTextSequence(new String[]{"The door is locked"});
        } else {
            System.out.println(getTasks());
            if (getTasks().isEmpty()) {
                if (open) {
                    queueRotation(app, 1f/SPEED, Vector3f.UNIT_Y, openAngle);
                } else {
                    queueRotation(app, 1f/SPEED, Vector3f.UNIT_Y, -openAngle);
                }
                open = !open;
            }
        }
    }
    
    public void deactivate() {
        locked = true;
    }
}
