/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.cam.echo2016.dynademo;

import com.jme3.input.controls.ActionListener;

/**
 *
 * @author Tim
 */
public interface DemoListener extends ActionListener {
    public void locEventAction(DemoLocEvent e);
}
