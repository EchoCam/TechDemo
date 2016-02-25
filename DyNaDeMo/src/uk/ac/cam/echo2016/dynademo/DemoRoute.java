
package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayList;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import com.jme3.shadow.AbstractShadowRenderer;

/**
 * @author tr393
 */
public class DemoRoute {
    private final String id;
    private final String sceneFile;
    private Vector3f startLoc;
    private Vector3f startDir;
    public ArrayList<DemoLocEvent> events = new ArrayList<DemoLocEvent>();
    public ArrayList<DemoLight> lights = new ArrayList<DemoLight>();
    public ArrayList<AbstractShadowRenderer> shadowRenderers = new ArrayList<AbstractShadowRenderer>();
    public ArrayList<Spatial> objects = new ArrayList<Spatial>();
    
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
}
