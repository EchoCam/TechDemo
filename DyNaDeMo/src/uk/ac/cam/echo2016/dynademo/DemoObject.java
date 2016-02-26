package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 * physicsType corresponds to:
 * <pre>
 *      0 - Static
 *      1 - Kinematic
 *      2 - Dynamic
 * </pre>
 * 
 * @author tr393
 */
public class DemoObject {

    public Spatial spatial;
    public ArrayList<DemoLight> lights = new ArrayList<>();
    public int physicsType;
    public float mass;
    public boolean isMainParent;

    /**
     * 
     * 
     * @param spatial - {@code Spatial} associated with this object
     * @param light - {@code DemoLight} used to light this object
     * @param physicsType - int PhysicsType corresponds to:
     * 
     * <pre>
     *      0 - Static
     *      1 - Kinematic
     *      2 - Dynamic
     * </pre>
     * 
     * @param mass - int mass is set to 0f if physicsType set to static
     * @param isMainParent - used to prevent separation of node with differnt physics types on load
     */
    public DemoObject(Spatial spatial, ArrayList<DemoLight> lights, int physicsType, float mass, boolean isMainParent) {
        this.spatial = spatial;
        this.lights = lights;
        this.physicsType = physicsType;
        this.mass = (physicsType == 0)? 0 : mass;
        this.isMainParent = isMainParent;
    }
}
