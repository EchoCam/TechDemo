package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 * Represents objects not part of the world mesh. Note specific lights must also be added to lights arraylist.
 * 
 * @author tr393
 */
public abstract class DemoObject {

    public Spatial spatial;
    public ArrayList<DemoLight> lights = new ArrayList<>();
    public float mass;
    public boolean isMainParent;

    /**
     * 
     * @param spatial - {@code Spatial} associated with this object
     * @param light - {@code DemoLight} used to light this object
     * @param mass - int mass is set to 0f if physicsType set to static
     * @param isMainParent - used to prevent separation of node with differnt physics types on load
     */
    public DemoObject(Spatial spatial, boolean isMainParent) {
        this.spatial = spatial;
        this.isMainParent = isMainParent;
    }
}
