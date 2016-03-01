package uk.ac.cam.echo2016.dynademo;

public class HeadMoveTask extends DemoTask {
    private HeadObject headObj;
    private boolean prevLight = false;
    private int movecount;
    private MainApplication app;
    
    public HeadMoveTask(String taskQueueId, float completionTime, HeadObject headOjb, MainApplication app) {
        super(taskQueueId, completionTime);
        this.headObj = headOjb;
        this.app = app;
    }
    
    @Override
    public void onTimeStep(float timePassed){
        if (app.getLightsOn() && prevLight == false) {
            System.out.println(app.getLightsOffCount());
            moveHead();
        }
        resetTime();
        prevLight = app.getLightsOn();
    }
    public void moveHead() {
        System.out.println("moved");
    }

}
