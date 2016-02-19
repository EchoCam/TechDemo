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
    public ArrayList<DemoLocEvent> events = new ArrayList<DemoLocEvent>();
    private Vector3f startCoord;
    
    public DemoRoute(Vector3f startCoord) {
        this.startCoord = startCoord;
    }
    public Vector3f getStartCoord() {
        return startCoord;
    }
}
