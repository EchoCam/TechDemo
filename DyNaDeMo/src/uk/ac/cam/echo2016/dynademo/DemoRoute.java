/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public class DemoRoute {
    private String id;
    public ArrayList<DemoLocEvent> events = new ArrayList<DemoLocEvent>();
    private Vector3f startLoc;
    private Vector3f startDir;
    
    public DemoRoute(String id, Vector3f startLoc, Vector3f startDir) {
        this.id = id;
        this.startLoc = startLoc;
        this.startDir = startDir;
    }
    public String getId() {
        return id;
    }
    public Vector3f getStartLoc() {
        return startLoc;
    }
    public Vector3f getStartDir() {
        return startDir;
    }
}
