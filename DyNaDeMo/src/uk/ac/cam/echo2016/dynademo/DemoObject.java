package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingVolume;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayList;

/**
 * Represents objects not part of the world mesh. Note specific lights must also be added to lights arraylist.
 * 
 * @author tr393
 */
public abstract class DemoObject {
    private final String objId;
    private Spatial spatial;
    private ArrayList<DemoLight> lights = new ArrayList<>();
    private float mass;
    private BoundingVolume bound;
    private Vector3f boundOffset;
    private boolean isMainParent;

    /**
     * 
     * @param spatial - {@code Spatial} associated with this object
     * @param light - {@code DemoLight} used to light this object
     * @param mass - int mass is set to 0f if physicsType set to static
     * @param isMainParent - used to prevent separation of node with differnt physics types on load
     * @param boundingVolume - Null for static objects
     */
    public DemoObject(String objId, Spatial spatial, boolean isMainParent, BoundingVolume bound) {
        this.objId = objId;
        this.spatial = spatial;
        this.bound = bound;
        this.boundOffset = (bound == null)? null : bound.getCenter().clone();
        
        this.isMainParent = isMainParent;
    }

    /**
     * @return the objId
     */
    public String getObjId() {
        return objId;
    }

    /**
     * @return the spatial
     */
    public Spatial getSpatial() {
        return spatial;
    }

    /**
     * @return the lights
     */
    public ArrayList<DemoLight> getLights() {
        return lights;
    }

    /**
     * @return the mass
     */
    public float getMass() {
        return mass;
    }

    /**
     * @param mass the mass to set
     */
    public void setMass(float mass) {
        this.mass = mass;
    }

    /**
     * @return the bound
     */
    public BoundingVolume getBound() {
        bound.setCenter(spatial.getWorldTranslation().add(boundOffset));
        return bound;
    }

    /**
     * @return the isMainParent
     */
    public boolean isIsMainParent() {
        return isMainParent;
    }

    /**
     * @param isMainParent the isMainParent to set
     */
    public void setIsMainParent(boolean isMainParent) {
        this.isMainParent = isMainParent;
    }
}
