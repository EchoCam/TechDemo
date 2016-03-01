package uk.ac.cam.echo2016.dynademo;

public class HeadMoveTask extends DemoTask {
    private HeadObject headObj;
    private boolean recentMove = false;
    private MainApplication app;
    
    public HeadMoveTask(String taskQueueId, float completionTime, HeadObject headOjb, MainApplication app) {
        super(taskQueueId, completionTime);
        this.headObj = headOjb;
    }
    
    @Override
    public void onTimeStep(float timePassed){
        if (app.getLightsOn()) {
            recentMove = false;
        } else {
            if (!recentMove && app.getLightsOffCount() > 1) {
                moveHead();
                
                
                
                recentMove = true;
            }
        }
        
    }
    public void moveHead() {
        System.out.println("moved");
    }

}
