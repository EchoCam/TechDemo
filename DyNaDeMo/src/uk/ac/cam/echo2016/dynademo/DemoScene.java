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
public class DemoScene {

    private final String id;
    private final String sceneFile;
    private ArrayList<Vector3f> startLocs = new ArrayList<Vector3f>();
    private ArrayList<Vector3f> startDirs = new ArrayList<Vector3f>();
    public BaseBundle properties = new BaseBundle();
    public ArrayList<ConditionEvent> condEvents = new ArrayList<>();
    public ArrayList<DemoLight> lights = new ArrayList<>();
    public HashMap<String, DemoLight> lightMap = new HashMap<>();
    public ArrayList<AbstractShadowRenderer> shadowRenderers = new ArrayList<>();
    public ArrayList<DemoObject> objects = new ArrayList<>();
//    public ArrayList<Spatial> dynamicObjects = new ArrayList<>();
//    public ArrayList<Spatial> kinematicObjects = new ArrayList<>();
//    public ArrayList<Spatial> staticObjects = new ArrayList<>();
    // public Node interactableNode = new Node("Interactables");
    public HashMap<Spatial, InteractionEvent> interactions = new HashMap<>();
    public String[] startupTextSequence = new String[0];

    public DemoScene(String id, String sceneFile, ArrayList<Vector3f> startLocs, ArrayList<Vector3f> startDirs) {
        this.id = id;
        this.sceneFile = sceneFile;
        if (startLocs != null && startDirs != null) {
            for (Vector3f v : startLocs) {
                this.startLocs.add(v);
            }
            for (Vector3f v : startDirs) {
                this.startDirs.add(v);
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getSceneFile() {
        return sceneFile;
    }

    public ArrayList<Vector3f> getStartLocs() {
        return startLocs;
    }

    public ArrayList<Vector3f> getStartDirs() {
        return startDirs;
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
    
    public void onLoad() {}
    public void onUnload() {}
}
