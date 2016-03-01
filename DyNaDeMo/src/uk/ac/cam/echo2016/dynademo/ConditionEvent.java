package uk.ac.cam.echo2016.dynademo;

/**
 * 
 * @author tr393
 */
public abstract class ConditionEvent extends DemoEvent {

    public ConditionEvent(String id) {
        super(id);
    }
    
    public abstract boolean checkCondition(MainApplication app);
}
