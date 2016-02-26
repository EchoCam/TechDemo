package uk.ac.cam.echo2016.dynademo;

import com.jme3.scene.Spatial;

/**
 * @author tr393
 */
public class DemoInteractEvent extends DemoEvent {
    private final DemoObject object;
    private final int interactType;

    /**
     * This is used to store events occuring on interaction, e.g. opening doors, pressing a button, etc.
     * 
     * @param id
     *            - Final String id of the event used to identify it.
     * @param spatial
     *            - The Spatial object to be affected by the event
     * @param interactType
     *            - An int corresponding as follows:
     * 
     *            <pre>
     *      0 - Drag event. e.g. carrying around crate
     *      1 - Translation event. e.g. pressure plate press
     * </pre>
     */
    public DemoInteractEvent(String id, DemoObject object, int interactType) {
        super(id);
        this.object = object;
        this.interactType = interactType;
    }

    public DemoObject getObject() {
        return object;
    }

    /**
     * Interactions types correspond to the following:
     * 
     * <pre>
     *      0 - Drag event. e.g. carrying around crate
     *      1 - Translation event. e.g. pressure plate press
     * </pre>
     * 
     * @return - The type of the interaction
     */
    public int getType() {
        return interactType;
    }
}
