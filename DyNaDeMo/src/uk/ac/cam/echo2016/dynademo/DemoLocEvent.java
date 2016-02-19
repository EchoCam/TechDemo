/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public class DemoLocEvent {
    public ArrayList<DemoListener> listeners = new ArrayList<DemoListener>();
    private final int id;
    Vector3f trigLoc;
    int trigWidth; // x
    int trigHeight; // y
    int trigDepth; // z
    public DemoLocEvent(int id, Vector3f loc, int width, int height, int depth) {
        this.id = id;
        trigLoc = loc;
        trigWidth = width;
        trigHeight = height;
        trigDepth = depth;
    }
    public int getId() {
        return id;
    }
    public boolean checkCondition(Vector3f playerLoc) {
        return playerLoc.x>trigLoc.x && playerLoc.x<trigLoc.x+trigWidth
            && playerLoc.y>trigLoc.y && playerLoc.y<trigLoc.y+trigHeight
            && playerLoc.z>trigLoc.z && playerLoc.z<trigLoc.z+trigDepth;
    }
    
    public void fireEvent() {
        for (DemoListener listener : listeners) {
            listener.locEventAction(this);
        }
    }
    
    
}
