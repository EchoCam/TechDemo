package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DemoInteractEvent extends DemoEvent {
    private final Spatial spatial;
    private final int interactType;
    public DemoInteractEvent(String id, Spatial spatial, int interactType) {
        super(id);
        this.spatial = spatial;
        this.interactType = interactType;
    }
    public Spatial getSpatial() {
        return spatial;
    }
    /**
     * Interactions types correspond to the following:
     * <pre>
     *      0 - Drag event. e.g. carrying around crate
     *      1 - Translation event. e.g. pressure plate press
     * </pre>
     * @return - The type of the interaction
     */
    public int getType() {
        return interactType;
    }
}
