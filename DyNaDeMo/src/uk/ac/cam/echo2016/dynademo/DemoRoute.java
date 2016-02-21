/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.light.Light;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public class DemoRoute {
    private final String id;
    private final String sceneFile;
    private Vector3f startLoc;
    private Vector3f startDir;
    public ArrayList<DemoLocEvent> events = new ArrayList<DemoLocEvent>();
    public ArrayList<Light> lights = new ArrayList<Light>();
    
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
