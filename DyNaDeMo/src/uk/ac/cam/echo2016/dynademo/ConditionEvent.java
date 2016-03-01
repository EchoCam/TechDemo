package uk.ac.cam.echo2016.dynademo;

/**
 * 
 * @author tr393
 */
public abstract class ConditionEvent extends DemoEvent {
    private boolean onceOnly = false;
    
    public ConditionEvent(String id, boolean onceOnly) {
        super(id);
        this.onceOnly = onceOnly;
    }
    
    @Override
    public final void onDemoEvent(MainApplication app) {
        if (onceOnly) app.getPollEventBus().remove(this);
        performAction(app);
    }
    
    public abstract void performAction(MainApplication app);
    
    public abstract boolean checkCondition(MainApplication app);
}
