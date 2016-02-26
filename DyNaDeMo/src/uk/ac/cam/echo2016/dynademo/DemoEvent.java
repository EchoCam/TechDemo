/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayList;

/**
 *
 * @author Tim
 */
public abstract class DemoEvent {
    private ArrayList<DemoListener> listeners = new ArrayList<>();
    private final String id;

    public DemoEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public ArrayList<DemoListener> getListeners() {
        return listeners;
    }

    public void addListener(DemoListener l) {
        listeners.add(l);
    }

    public void fireEvent() {
        for (DemoListener listener : listeners) {
            listener.demoEventAction(this);
        }
    }

}
