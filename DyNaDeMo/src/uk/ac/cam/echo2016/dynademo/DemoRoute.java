package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayList;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.AbstractShadowRenderer;
import java.util.HashMap;

/**
 * @author tr393
 */
public class DemoRoute {

    private final String id;
    private final String sceneFile;
    private Vector3f startLoc;
    private Vector3f startDir;
    public ArrayList<DemoLocEvent> events = new ArrayList<>();
    public ArrayList<DemoLight> lights = new ArrayList<>();
    public ArrayList<AbstractShadowRenderer> shadowRenderers = new ArrayList<>();
    public ArrayList<Spatial> dynamicObjects = new ArrayList<>();
    public ArrayList<Spatial> kinematicObjects = new ArrayList<>();
    public ArrayList<Spatial> staticObjects = new ArrayList<>();
    // public Node interactableNode = new Node("Interactables");
    public HashMap<Spatial, DemoInteractEvent> interactions = new HashMap<>();

    public DemoRoute(String id, String sceneFile, Vector3f startLoc, Vector3f startDir) {
        this.id = id;
        this.sceneFile = sceneFile;
        this.startLoc = startLoc;
        this.startDir = startDir;
    }

    public String getId() {
        return id;
    }

    public String getSceneFile() {
        return sceneFile;
    }

    public Vector3f getStartLoc() {
        return startLoc;
    }

    public Vector3f getStartDir() {
        return startDir;
    }

    public void setInteractable(Spatial s, DemoInteractEvent e) {
        // interactableNode.attachChild(s);
        interactions.put(s, e);
    }

    /**
     * Fires a {@code DemoInteractEvent} if the spatial or any of its parents is set to be interactable. If multiple
     * ancestors are interactable, only the closest one (fewest connnections) will be interacted with.
     *
     * @param spatial
     */
    public boolean interactWith(Spatial spatial) {
        DemoInteractEvent e = interactions.get(spatial);
        if (e != null) {
            e.fireEvent();
            return true;
        } else {
            Node parent = spatial.getParent();
            if (parent != null) {
                interactWith(parent);
            }
        }
        return false;
    }
}
