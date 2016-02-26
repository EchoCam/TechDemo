package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayDeque;
import java.util.List;

import uk.ac.cam.echo2016.dynademo.screens.CharacterSelectScreen;
import uk.ac.cam.echo2016.dynademo.screens.GameScreen;
import uk.ac.cam.echo2016.dynademo.screens.MainMenuScreen;
import uk.ac.cam.echo2016.dynademo.screens.PauseMenuScreen;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.AbstractShadowRenderer;

import de.lessvoid.nifty.Nifty;
import java.util.HashMap;

/**
 * @author tr393
 */
@SuppressWarnings("deprecation")
public class MainApplication extends SimpleApplication implements DemoListener {

    public final static float CHARHEIGHT = 3;
    public HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();
    
    private Node playerNode;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private GhostControl billMurray;
    
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
    private Nifty nifty;
    //Screens
    private MainMenuScreen mainMenuScreen;
    private CharacterSelectScreen characterSelectScreen;
    private PauseMenuScreen pauseMenuScreen;
    private GameScreen gameScreen;

    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        app.start();
    }

    public MainApplication() {
        super();
    }

    @Override
    public void simpleInitApp() {
        // Set-Up for the main menu //
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(
                assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);

        nifty.fromXml("Interface/Nifty/mainMenu.xml", "mainMenu", new MainMenuScreen().init(stateManager, this));
        nifty.addXml("Interface/Nifty/characterSelect.xml");
        nifty.addXml("Interface/Nifty/pauseMenu.xml");
        nifty.addXml("Interface/Nifty/game.xml");


        mainMenuScreen = (MainMenuScreen) nifty.getScreen("mainMenu").getScreenController();
        characterSelectScreen = (CharacterSelectScreen) nifty.getScreen("characterSelect").getScreenController();
        pauseMenuScreen = (PauseMenuScreen) nifty.getScreen("pauseMenu").getScreenController();
        gameScreen = (GameScreen) nifty.getScreen("game").getScreenController();

        stateManager.attach(mainMenuScreen);
        stateManager.attach(characterSelectScreen);
        stateManager.attach(pauseMenuScreen);
        stateManager.attach(gameScreen);

        //TODO(tr395): find way to make it so that onStartScreen() isn't called until this point.
        nifty.gotoScreen("mainMenu");


        // Application related setup //
        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setupKeys();

        // Setup world and locEvents //
        routes = Initialiser.initialiseRoutes(this);

        // Initialize physics engine //
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        // Add global Lights //

        AmbientLight al = new AmbientLight(); // No current effect on blender scene
        al.setColor(new ColorRGBA(0.1f, 0.1f, 0.1f, 1f));
        rootNode.addLight(al);
        rootNode.setShadowMode(ShadowMode.CastAndReceive);

        // Initialize World (as a placeholder) //
        currentWorld = assetManager.loadModel("Scenes/Bedroom.j3o"); // Not used - reloaded later
        currentWorld.scale(10f);
        rootNode.attachChild(currentWorld);
        // Make a rigid body from the scene //
        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentWorld);
        landscape = new RigidBodyControl(sceneShape, 0f);
        currentWorld.addControl(landscape);
        landscape.setFriction(1f);
        bulletAppState.getPhysicsSpace().add(landscape);

        // Load Character into world //
        playerNode = new Node("playerNode");
        rootNode.attachChild(playerNode);
        
        // Attach physic control to the character
        playerControl = new CharacterControl(
                new CapsuleCollisionShape(CHARHEIGHT / 2, CHARHEIGHT, 1), 0.2f);
        playerControl.setJumpSpeed(20f);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(50);
        playerControl.setPhysicsLocation(new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0)); // 2.5f vertical lee-way
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);
        
        // Attach ghost control to the character
        billMurray = new GhostControl(
                new CapsuleCollisionShape((CHARHEIGHT / 2), CHARHEIGHT , 1));
        playerNode.addControl(billMurray);
        bulletAppState.getPhysicsSpace().add(billMurray);
        
        // Start at Area 0 //
        currentRoute = routes.get("Bedroom");
        enterLocation(currentRoute);
    }

    private void enterLocation(DemoRoute route) {
        // Unload old route (currentRoute)
        currentWorld.removeControl(landscape);
        currentWorld.removeFromParent();
        bulletAppState.getPhysicsSpace().remove(landscape);
        
        for (Spatial object : currentRoute.objects) {
            rootNode.detachChild(object);
        }
        for (DemoLight l : currentRoute.lights) { // FIXME should do a search
            rootNode.removeLight(l.light);
        }
        for (AbstractShadowRenderer plsr : currentRoute.shadowRenderers) {
            viewPort.removeProcessor(plsr);
        }
        for (DemoLocEvent oldEvent : currentRoute.events) {
            locEventQueue.remove(oldEvent);
        }
        this.currentRoute = route;
        
        // Load new route (route)
        currentWorld = assetManager.loadModel(route.getSceneFile());
        currentWorld.scale(10f);
        rootNode.attachChild(currentWorld);
        
        // Load route objects and add rigidbodycontrols
        for (Spatial object : route.objects) {
            rootNode.attachChild(object);
            RigidBodyControl rbc = new RigidBodyControl(5f);
            object.addControl(rbc);
            rbc.setFriction(1.5f);
            bulletAppState.getPhysicsSpace().add(rbc);
        }
        for (DemoLight l : route.lights) {
        	for(String spatialName: l.spatialNames) {
        		List<Spatial> list = rootNode.descendantMatches(spatialName);
        		if (list.isEmpty()) {System.out.println("Spatial not found!");}
        		Spatial spatial = list.get(0);
        		spatial.addLight(l.light);
        	}
        }
        for (AbstractShadowRenderer plsr : route.shadowRenderers) {
//            viewPort.addProcessor(plsr); // Disabled for now
        }
        for (DemoLocEvent newEvent : route.events) {
            locEventQueue.add(newEvent);
        }

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentWorld);
        landscape = new RigidBodyControl(sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);

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
        inputManager.addMapping("Interact", new KeyTrigger(KeyInput.KEY_E));
//        inputManager.deleteMapping(INPUT_MAPPING_EXIT); //TODO replace with pause
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));


        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "I");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");
    }

    @Override
    public void simpleUpdate(float tpf) {
        if (!isPaused) {
            // Find direction of camera (and rotation)
            camDir.set(cam.getDirection().multLocal(.4f));
            camLeft.set(cam.getLeft().multLocal(.4f));
            // Calculate distance to move
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
            playerControl.setWalkDirection(walkDirection.mult(60f*tpf));
            // Move camera to correspond to player
            cam.setLocation(playerControl.getPhysicsLocation().add(0, CHARHEIGHT / 2 + 1f, 0));
            // Check character for collisions
            for(PhysicsCollisionObject object : billMurray.getOverlappingObjects()) {
                if (object instanceof RigidBodyControl)
                    ((RigidBodyControl) object).applyCentralForce(camDir.mult(1000f));
            }
            // Check global event queue
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
        } else if (keyName.equals("Interact")) {
            // Ray Casting (checking for first interactable object)
            Ray ray = new Ray(cam.getLocation(),cam.getDirection());
            CollisionResults results = new CollisionResults();
            currentRoute.interactables.collideWith(ray, results);
            CollisionResult closest = results.getClosestCollision();
            closest.getGeometry().getName(); // TODO finish impl
        } else if (keyName.equals("Jump")) {
            if (isPressed) {
                playerControl.jump();
            }
        } else if (keyName.equals("Pause")) {
            if (isPressed) {
                if (!isPaused) {
                    nifty.gotoScreen("pauseMenu");
                } else {
                    nifty.gotoScreen("game");
                }
            }
        }
    }

    public void setIsPaused(boolean isPaused) {
        this.isPaused = isPaused;
    }

    public void locEventAction(DemoLocEvent e) {
        switch (e.getId()) {
            case 0: // TODO first meeting
                enterLocation(routes.get("PuzzleRoom")); // temp functionality
        }
    }

    public MainMenuScreen getMainMenuScreen() {
        return mainMenuScreen;
    }

    public PauseMenuScreen getPauseMenuScreen() {
        return pauseMenuScreen;
    }

    public CharacterSelectScreen getCharacterSelectScreen() {
        return characterSelectScreen;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }
    
    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void chooseRoute() {
        // TODO add calls to our tools here
        //currentRoute = ...
        //enterLocation(...)
    }
}
