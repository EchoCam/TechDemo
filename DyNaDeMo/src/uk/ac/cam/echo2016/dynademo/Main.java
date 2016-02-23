package uk.ac.cam.echo2016.dynademo;

import uk.ac.cam.echo2016.dynademo.screens.MainMenuScreen;
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
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;
import de.lessvoid.nifty.Nifty;
import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * @author tr93
 */
public class Main extends SimpleApplication implements DemoListener {

    private final static float CHARHEIGHT = 3;
    public ArrayList<DemoRoute> routes = new ArrayList<DemoRoute>();
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private boolean keyLeft = false, keyRight = false, keyUp = false, keyDown = false;
    private boolean isPaused = false;
    NiftyJmeDisplay pauseDisplay;
    private ArrayDeque<DemoLocEvent> locEventQueue = new ArrayDeque<DemoLocEvent>();
    private Spatial currentWorld;
    private DemoRoute currentRoute;
    //private currentRoute/Character

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    public Main() {
        super();
    }

    @Override
    public void simpleInitApp() {

        // Set-Up for the main menu //
        NiftyJmeDisplay mainMenuNiftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        Nifty mainMenuNifty = mainMenuNiftyDisplay.getNifty();
        guiViewPort.addProcessor(mainMenuNiftyDisplay);

        //TODO(tr395) work out how to properly encapsulate this stuff
        flyCam.setDragToRotate(true); // you need the mouse for clicking now
        MainMenuScreen mainMenuScreen = new MainMenuScreen();
        stateManager.attach(mainMenuScreen);
        mainMenuNifty.fromXml("Interface/Nifty/mainMenu/screen.xml", "start", mainMenuScreen);

        // Application related setup //
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setupKeys();

        // Setup world and locEvents //
        initializeAreas();

        // Initialize physics engine //
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Add global Lights //

        AmbientLight al = new AmbientLight(); // No current effect on blender scene
        al.setColor(new ColorRGBA(0.1f,0.1f,0.1f,1f));
        rootNode.addLight(al);

//        SpotLight light1 = new SpotLight();
//        light1.setDirection(new Vector3f(0f,-3f, 0f));
//        light1.setColor(ColorRGBA.White.mult(5f));
//        light1.setPosition(new Vector3f(0, 500, 0));
//        light1.setSpotInnerAngle(0f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
//        light1.setSpotOuterAngle(120f * FastMath.DEG_TO_RAD);
//        light1.setSpotRange(1000f);
//        rootNode.addLight(light1);

        PointLight light2 = new PointLight();
        light2.setColor(ColorRGBA.Gray);
        light2.setPosition(new Vector3f(0, 5, 0));
        light2.setRadius(1000f);
        rootNode.addLight(light2);

//        DirectionalLight light3 = new DirectionalLight();
//        light3.setDirection(Vector3f.UNIT_Y.negate());
//        light3.setColor(ColorRGBA.White);
//        rootNode.addLight(light3);

        // TODO add more lights

        // Add shadow renderer //
        PointLightShadowRenderer plsr = new PointLightShadowRenderer(assetManager, 1024);
        plsr.setLight(light2);
        // bit dodgy - TODO fix walls and textures affected by this

        viewPort.addProcessor(plsr);
        rootNode.setShadowMode(ShadowMode.CastAndReceive);

        // Initialize World //
        currentWorld = assetManager.loadModel("Scenes/Scene1.j3o"); // Not used - reloaded later
        currentWorld.scale(10f);
        rootNode.attachChild(currentWorld);
        // Make a rigid body from the scene //
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentWorld);
        landscape = new RigidBodyControl(sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);

        // Load Character into world //
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(CHARHEIGHT / 2, CHARHEIGHT, 1);
        playerControl = new CharacterControl(capsuleShape, 0.2f);
        playerControl.setJumpSpeed(20);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(50);
        playerControl.setPhysicsLocation(new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0)); // 2.5f vertical lee-way
        bulletAppState.getPhysicsSpace().add(playerControl);

        // Start at Area 0 //
        currentRoute = routes.get(0);
        enterLocation(currentRoute);
    }

    private void initializeAreas() {
        // TODO aassociate lights to routes
        DemoRoute area;
        DemoLocEvent e;

        // First Route
        area = new DemoRoute("StartRoute", "Scenes/Scene1.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(1, 0, 0));
        // Starting meeting Event
        e = new DemoLocEvent(0, new Vector3f(-80, 1, -40), 40, 14, 50);
        e.listeners.add(this);
        area.events.add(e);
        routes.add(area);

        // Second Route
        area = new DemoRoute(("Route2"), "Scenes/Scene2.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(-1, 0, 0));
        routes.add(area);
    }

    private void enterLocation(DemoRoute route) {
        // Unload and Load the old and new routes
        currentWorld.removeControl(landscape);
        currentWorld.removeFromParent();
        bulletAppState.getPhysicsSpace().remove(landscape);

        currentWorld = assetManager.loadModel(route.getSceneFile());
        currentWorld.scale(10f);
        rootNode.attachChild(currentWorld);

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentWorld);
        landscape = new RigidBodyControl(sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);

        // Update events to the new route
        for (DemoLocEvent oldEvent : currentRoute.events) {
            locEventQueue.remove(oldEvent);
        }
        for (DemoLocEvent newEvent : route.events) {
            locEventQueue.add(newEvent);
        }
        this.currentRoute = route;

        // TODO freeze for a second
        playerControl.setPhysicsLocation(route.getStartLoc());
        cam.lookAtDirection(route.getStartDir(), Vector3f.UNIT_Y);
        // TODO other initializations

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
//        inputManager.deleteMapping(INPUT_MAPPING_EXIT); //TODO replace with pause
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));


        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!isPaused) {
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
            cam.setLocation(playerControl.getPhysicsLocation().add(0, CHARHEIGHT / 2 + 1f, 0));

            //        System.out.println(playerControl.getPhysicsLocation().x);
            //        System.out.println(playerControl.getPhysicsLocation().y);
            //        System.out.println(playerControl.getPhysicsLocation().z);
            //        System.out.println();

            for (DemoLocEvent e : locEventQueue) {
                if (e.checkCondition(playerControl.getPhysicsLocation())) {
                    e.fireEvent();
                }
            }
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
            if (isPressed) {
                playerControl.jump();
            }
        } else if (keyName.equals("Pause")) {
            if (isPressed) {
                if (!isPaused) {
                    // Bring up pause menu //
                } else {
                    // Close pause menu
                }
                isPaused = !isPaused;
            }
        }
    }

    public void locEventAction(DemoLocEvent e) {
        switch (e.getId()) {
            case 0: // TODO first meeting
                enterLocation(routes.get(1)); // temp functionality
        }
    }

    public void chooseRoute() {
        // TODO add calls to our tools here
        //currentRoute = ...
        //enterLocation(...)
    }
}
