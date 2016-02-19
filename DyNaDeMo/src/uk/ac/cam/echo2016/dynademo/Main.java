package uk.ac.cam.echo2016.dynademo;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * @author tr93
 */
public class Main extends SimpleApplication implements DemoListener {
    
    private final static float CHARHEIGHT = 3;
    public ArrayList<DemoRoute> areas = new ArrayList<DemoRoute>();
    
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private boolean keyLeft = false, keyRight = false, keyUp = false, keyDown = false;
    
    private ArrayDeque<DemoLocEvent> locEventQueue = new ArrayDeque<DemoLocEvent>();
    private DemoRoute currentArea;
    //private currentRoute/Character
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
//        /**
//         * Activate the Nifty-JME integration: 
//         */
//        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
//                assetManager, inputManager, audioRenderer, guiViewPort);
//        Nifty nifty = niftyDisplay.getNifty();
//        guiViewPort.addProcessor(niftyDisplay);
//        flyCam.setDragToRotate(true); // you need the mouse for clicking now    
//        //nifty.setDebugOptionPanelColors(true);
//        nifty.fromXml("Interface/tutorial/screen2.xml", "start");
        

        // Application related setup
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
//        flyCam.setMoveSpeed(40f);
        setupKeys();
        
        // Setup world and locEvents
        initializeAreas();
        
        // initialize physics engine
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        
        // Add global Lights
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
        
        DirectionalLight d2 = new DirectionalLight();
        d2.setColor(ColorRGBA.White);
        
        d2.setDirection(new Vector3f(0f, -2.8f, 0f).normalizeLocal());
        rootNode.addLight(d2);

        
        // Load World
        Spatial scene = assetManager.loadModel("Scenes/Scene_v102.j3o");
//        Spatial scene = assetManager.loadModel("Textures/Table.mesh.xml.scene");
        scene.scale(10f);
        rootNode.attachChild(scene);
        
        // Load Character into world
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(scene);
        landscape = new RigidBodyControl(sceneShape, 0f);
        scene.addControl(landscape);
        
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(CHARHEIGHT/2, CHARHEIGHT, 1);
        playerControl = new CharacterControl(capsuleShape, 0.2f);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(50);
        playerControl.setPhysicsLocation(new Vector3f(0, (CHARHEIGHT/2)+2.5f, 0)); // 2.5f vertical lee-way
        bulletAppState.getPhysicsSpace().add(landscape);
        bulletAppState.getPhysicsSpace().add(playerControl);
        
        enterLocation(areas.get(0));
    }
    
    private void initializeAreas() { // this is event source and listener
        DemoRoute area;
        DemoLocEvent e;
        area = new DemoRoute(new Vector3f(0,0,0));
        // Starting meeting Event
        e = new DemoLocEvent(0, new Vector3f(-80,1,-40), 40, 14, 50);
        e.listeners.add(this);
        area.events.add(e);
        areas.add(area);
    }
    
    
    private void enterLocation(DemoRoute area) {
        
        for(DemoLocEvent oldEvent  : area.events) {
            locEventQueue.remove(oldEvent);
        }
        this.currentArea = area;
        for(DemoLocEvent newEvent  : area.events) {
            locEventQueue.add(newEvent);
        }
        // TODO move player and stuff
    }
    
    private void setupKeys() {
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S));
        
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
    }
    
    @Override
    public void simpleUpdate(float tpf) {
        camDir.set(cam.getDirection().multLocal(.4f));
        camLeft.set(cam.getLeft().multLocal(.4f));
        walkDirection.set(0, 0, 0);
        if (keyLeft) {
            walkDirection.addLocal(camLeft);
        }
        if (keyRight) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (keyUp) {
            walkDirection.addLocal(camDir.x, 0, camDir.z);
        }
        if (keyDown) {
            walkDirection.addLocal(-camDir.x, 0, -camDir.z);
        }
        playerControl.setWalkDirection(walkDirection);
        cam.setLocation(playerControl.getPhysicsLocation().add(0, CHARHEIGHT/2 + 1f, 0));
        
        System.out.println(playerControl.getPhysicsLocation().x);
        System.out.println(playerControl.getPhysicsLocation().y);
        System.out.println(playerControl.getPhysicsLocation().z);
        System.out.println();
        for(DemoLocEvent e : locEventQueue) {
            if (e.checkCondition(playerControl.getPhysicsLocation())) e.fireEvent();
        }
    }
    
    @Override
    public void onAction(String keyName, boolean isPressed, float tpf) {
        if (keyName.equals("Left")) {
            keyLeft = isPressed;
        } else if (keyName.equals("Right")) {
            keyRight = isPressed;
        } else if (keyName.equals("Up")) {
            keyUp = isPressed;
        } else if (keyName.equals("Down")) {
            keyDown = isPressed;
        } else if (keyName.equals("Jump")) {
            if (isPressed) playerControl.jump();
        }
    }

    public void locEventAction(DemoLocEvent e) {
        switch (e.getId()) {
            case 0:
                throw new UnsupportedOperationException("First event goes here!");
                // TODO first meeting
        }
    }
}
