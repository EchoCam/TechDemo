package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DoorObject extends KinematicDemoObject implements InteractableObject{
    public final static float SPEED = 30f;
    private boolean open = false;
    private Vector3f openPos;
    private Vector3f closePos;

    public DoorObject(String objId, Spatial spatial, float mass, boolean isMainParent, BoundingVolume bound, Vector3f openPos, Vector3f closePos) {
        super(objId, spatial, mass, isMainParent, bound);
        this.openPos = openPos;
        this.closePos = closePos;
    }

    @Override
    public void interact(MainApplication app) {
        System.out.println(getTasks());
        if (getTasks().isEmpty()) {
            if (open) {
                queueRotation(app, 2f, Vector3f.UNIT_Y, FastMath.PI*2/3);
            } else {
                queueRotation(app, 2f, Vector3f.UNIT_Y, FastMath.PI*-2/3);
            }
            open = !open;
        }
    }
}
