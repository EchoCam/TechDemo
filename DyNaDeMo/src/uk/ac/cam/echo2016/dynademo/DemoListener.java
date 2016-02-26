package uk.ac.cam.echo2016.dynademo;

import com.jme3.input.controls.ActionListener;

/**
 *
 * @author tr393
 */
public interface DemoListener extends ActionListener {
    public void demoEventAction(DemoEvent e);
}
