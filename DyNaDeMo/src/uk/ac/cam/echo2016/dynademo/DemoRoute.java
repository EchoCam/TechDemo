package uk.ac.cam.echo2016.dynademo;

import android.os.BaseBundle;
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
    public BaseBundle properties = new BaseBundle();
    public ArrayList<LocationEvent> locEvents = new ArrayList<>();
    public ArrayList<DemoLight> lights = new ArrayList<>();
    public ArrayList<AbstractShadowRenderer> shadowRenderers = new ArrayList<>();
    public ArrayList<DemoObject> objects = new ArrayList<>();
//    public ArrayList<Spatial> dynamicObjects = new ArrayList<>();
//    public ArrayList<Spatial> kinematicObjects = new ArrayList<>();
//    public ArrayList<Spatial> staticObjects = new ArrayList<>();
    // public Node interactableNode = new Node("Interactables");
    public HashMap<Spatial, InteractionEvent> interactions = new HashMap<>();
    public String[] startupTextSequence = new String[0];

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

    /**
     * Added the pair to a hash map used to find an object's event
     *
     * @param s
     * @param e
     */
    public void setInteractable(Spatial s, InteractionEvent e) {
        // interactableNode.attachChild(s);
        interactions.put(s, e);
    }

    /**
     * Fires a {@code DemoInteractEvent} if the spatial or any of its parents is set to be interactable. If multiple
     * ancestors are interactable, only the closest one (fewest connnections) will be interacted with.
     *
     * @param spatial - The spatial or parent that maps to an {@code DemoInteractEvent}
     */
    public boolean interactWith(MainApplication app, Spatial spatial) {
        InteractionEvent e = interactions.get(spatial);
        if (e != null) {
            e.onDemoEvent(app);
            return true;
        } else {
            Node parent = spatial.getParent();
            if (parent != null) {
                return interactWith(app, parent);
            }
        }
        return false;
    }
}
