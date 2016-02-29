package uk.ac.cam.echo2016.dynademo;

import java.util.ArrayDeque;
import java.util.List;

import uk.ac.cam.echo2016.dynademo.screens.CharacterSelectScreen;
import uk.ac.cam.echo2016.dynademo.screens.GameScreen;
import uk.ac.cam.echo2016.dynademo.screens.PauseMenuScreen;
import uk.ac.cam.echo2016.dynademo.screens.MainMenuScreen;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.bounding.BoundingBox;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionObject;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.GhostControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.objects.PhysicsRigidBody;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.shadow.AbstractShadowRenderer;
import com.jme3.system.AppSettings;
import de.lessvoid.nifty.Nifty;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.dynademo.screens.DialogueScreen;
import uk.ac.cam.echo2016.multinarrative.InvalidGraphException;
import uk.ac.cam.echo2016.multinarrative.NarrativeInstance;
import uk.ac.cam.echo2016.multinarrative.NarrativeTemplate;
import uk.ac.cam.echo2016.multinarrative.io.SaveReader;

/**
 * The God class of the game.
 * 
 * Although ideally this class wouldn't implement as much as it does, because our focus
 * for this game is to create a rough and ready tech-demo, it will suffice.
 * 
 * This class can essentially "see" and "control" everything, although a lot of functionality
 * has been delegated to other classes where possible.
 * 
 * @author tr393
 */
@SuppressWarnings("deprecation")
public class MainApplication extends SimpleApplication implements ActionListener {

    public final static float HALFCHARHEIGHT = 3;
    public HashMap<String, DemoRoute> routes = new HashMap<>();
    private Node playerNode;
    private BulletAppState bulletAppState;
    private RigidBodyControl landscape;
    private CharacterControl playerControl;
    private GhostControl billMurray;
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    private Vector3f walkDirection = new Vector3f();
    private DemoObject draggedObject;
    private boolean keyLeft = false, keyRight = false, keyUp = false, keyDown = false;
    private boolean isPaused = false;
    NiftyJmeDisplay pauseDisplay;
    private ArrayDeque<LocationEvent> locEventBus = new ArrayDeque<>();
    private HashMap<String, ArrayDeque<DemoTask>> taskEventBus = new HashMap<>();
    private Spatial currentWorld;
    private DemoRoute currentRoute;
    // private currentRoute/Character
    public Nifty nifty;
    // Screens
    private MainMenuScreen mainMenuScreen;
    private CharacterSelectScreen characterSelectScreen;
    private PauseMenuScreen pauseMenuScreen;
    private GameScreen gameScreen;
    private DialogueScreen dialogueScreen;
    private NarrativeInstance narrativeInstance;
    private DemoDialogue dialogue;

    /**
     * The main entry point for the code of the game.
     * 
     * Mainly initialises an instant of the game where the framerate is set to 60FPS.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        MainApplication app = new MainApplication();
        AppSettings settings = new AppSettings(true);
        settings.setFrameRate(60);
        app.setSettings(settings);
        app.start();
    }

    
    /**
     * Called when an instant of the game (this) is created.
     * 
     * Loads in our DyNaMo .dnm file which contains the structure of our narrative,
     * entirely using our tools.
     * 
     */
    public MainApplication() {
        super();
        try {
            InputStream is = this.getClass().getResourceAsStream("dynademo.dnm");
            NarrativeTemplate narrativeTemplate = SaveReader.loadNarrativeTemplate(is);
            narrativeInstance = narrativeTemplate.generateInstance();
        } catch (IOException | InvalidGraphException ex) {
            Logger.getLogger(MainApplication.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }

    }

    /**
     * A getter to get access to the narrativeInstance.
     * 
     * Mainly used by the character select screen, so it can determine which characters to
     * show based on the available routes.
     * 
     * @return 
     */
    public NarrativeInstance getNarrativeInstance() {
        return narrativeInstance;
    }

    /**
     * A method called when this.start() is.
     * 
     * Where most of the member variables are initialised. See comments in method for more.
     */
    @Override
    public void simpleInitApp() {
        // Set-Up for all the screens //
        // initialise nifty gui, the tools we are using for gui elements
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        guiViewPort.addProcessor(niftyDisplay);

        // load in screens as defined in our XML files
        nifty.fromXml("Interface/Nifty/mainMenu.xml", "mainMenu", new MainMenuScreen().init(stateManager, this));
        nifty.addXml("Interface/Nifty/characterSelect.xml");
        nifty.addXml("Interface/Nifty/pauseMenu.xml");
        nifty.addXml("Interface/Nifty/game.xml");
        nifty.addXml("Interface/Nifty/dialogue.xml");

        
        // make the screens accesible from within the application
        mainMenuScreen = (MainMenuScreen) nifty.getScreen("mainMenu").getScreenController();
        characterSelectScreen = (CharacterSelectScreen) nifty.getScreen("characterSelect").getScreenController();
        pauseMenuScreen = (PauseMenuScreen) nifty.getScreen("pauseMenu").getScreenController();
        gameScreen = (GameScreen) nifty.getScreen("game").getScreenController();
        dialogueScreen = (DialogueScreen) nifty.getScreen("dialogue").getScreenController();

        // initialise the screens as states as well (mainly to give acces to this class instance within them)
        stateManager.attach(mainMenuScreen);
        stateManager.attach(characterSelectScreen);
        stateManager.attach(pauseMenuScreen);
        stateManager.attach(gameScreen);
        stateManager.attach(dialogueScreen);


        // start the game at the main menu!
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

        // Placeholder routes for later initialization
        currentWorld = new Geometry("placeholder", new Box(1, 1, 1));
        rootNode.attachChild(currentWorld);
        landscape = new RigidBodyControl();//sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);
        currentRoute = new DemoRoute("", "", null, null);

        // Load character //
        playerNode = new Node("playerNode");
        rootNode.attachChild(playerNode);

        // Attach physic control to the character
        CapsuleCollisionShape capsule = new CapsuleCollisionShape(HALFCHARHEIGHT / 2, HALFCHARHEIGHT, 1);
        playerControl = new CharacterControl(capsule, 0.25f);
        playerControl.setJumpSpeed(20f);
        playerControl.setFallSpeed(30);
        playerControl.setGravity(50);
        playerControl.setPhysicsLocation(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0)); // 1.0f off the ground
        playerNode.addControl(playerControl);
        bulletAppState.getPhysicsSpace().add(playerControl);

        // Attach ghost control to the character
        billMurray = new GhostControl(capsule);
        playerNode.addControl(billMurray);
        bulletAppState.getPhysicsSpace().add(billMurray);

        // Start at Area 0 //
        loadRoute(routes.get("BedroomRoute"), 0);

        // Debug Options//
        bulletAppState.setDebugEnabled(true);
//
//        Geometry g = new Geometry("wireframe cube", new WireBox(HALFCHARHEIGHT / 2, HALFCHARHEIGHT, HALFCHARHEIGHT / 2));
//        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
//        mat.getAdditionalRenderState().setWireframe(true);
//        mat.setColor("Color", ColorRGBA.Green);
//        g.setMaterial(mat);
//        playerNode.attachChild(g);
    }

    public void loadRoute(DemoRoute route, int entIndex) {
        // Unload old route (currentRoute)
        currentWorld.removeControl(landscape);
        currentWorld.removeFromParent();
        bulletAppState.getPhysicsSpace().remove(landscape);
        for (DemoObject object : currentRoute.objects) {
            // TODO clean up lights not being removed from rooms?
            Spatial spatial = object.getSpatial();
            if (object.isIsMainParent()) {
                rootNode.detachChild(spatial);
            }
            RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
            spatial.removeControl(rbc);
            for (DemoLight dLight : object.getLights()) {
                spatial.removeLight(dLight.light);
            }
        }
        // TODO replace with neater method?
        for (PhysicsRigidBody r : bulletAppState.getPhysicsSpace().getRigidBodyList()) {
            bulletAppState.getPhysicsSpace().remove(r);
        }
        
        for (DemoLight l : currentRoute.lights) { // FIXME should do a search
            rootNode.removeLight(l.light);
        }
        for (AbstractShadowRenderer plsr : currentRoute.shadowRenderers) {
            viewPort.removeProcessor(plsr);
        }
        for (LocationEvent oldEvent : currentRoute.locEvents) {
            locEventBus.remove(oldEvent);
        }

        // Load new route (route)
        currentRoute = route;
        currentWorld = assetManager.loadModel(route.getSceneFile());
        currentWorld.scale(10f);
        rootNode.attachChild(currentWorld);

        // Load route objects and add rigidbodycontrols
        for (DemoObject object : route.objects) {
            if (object.isIsMainParent()) {
                rootNode.attachChild(object.getSpatial());
            }

            RigidBodyControl rbc = new RigidBodyControl(object.getMass());
            object.getSpatial().addControl(rbc);
            if (object instanceof KinematicDemoObject) {
                rbc.setKinematic(true);
            }
            if (object instanceof DynamicDemoObject) {
                rbc.setFriction(1.5f);
            }
            bulletAppState.getPhysicsSpace().add(rbc);
            for (DemoLight dLight : object.getLights()) {
                object.getSpatial().addLight(dLight.light);
            }
        }
        
        // Load dragged object
        if (draggedObject != null) {
//            playerNode.attachChild(draggedSpatial.getSpatial());
            RigidBodyControl rbc = new RigidBodyControl(draggedObject.getMass());
            draggedObject.getSpatial().addControl(rbc);
            if (draggedObject instanceof KinematicDemoObject) {
                rbc.setKinematic(true);
            }
            if (draggedObject instanceof DynamicDemoObject) {
                rbc.setFriction(1.5f);
                
            }
//            bulletAppState.getPhysicsSpace().add(rbc);
//            this.currentRoute.objects.add(draggedObject);
//            this.currentRoute.interactions.put(spatial, previousRoute.interactions.get(draggedObject))
            for (DemoLight dLight : currentRoute.lights) {
                draggedObject.getSpatial().addLight(dLight.light);
            }
            playerNode.attachChild(draggedObject.getSpatial());
        }
        // TODO this the proper way
        if (currentRoute.getId().equals("PuzzleRoute") && !gameScreen.getRoute().equals("Puzzle again")) {
            for(DemoObject object: currentRoute.objects) {
                if (object.getObjId().equals("crate2")) {
                    RigidBodyControl rbc = object.getSpatial().getControl(RigidBodyControl.class);
                    bulletAppState.getPhysicsSpace().remove(rbc);
                    object.getSpatial().removeControl(rbc);
                    
                    rootNode.detachChild(object.getSpatial());
                    
                    for (DemoLight dLight : object.getLights()) {
                        object.getSpatial().removeLight(dLight.light);
                    }
                }
            }
        }
            
        
        for (DemoLight l : route.lights) {
            for (String roomName : l.affectedRooms) {
                // TODO hacky
                List<Spatial> list = rootNode.descendantMatches(roomName);
                if (list.isEmpty()) {
                    throw new RuntimeException("Room " + roomName + " not found!");
                }
                Spatial room = list.get(0);
                room.addLight(l.light);
            }
        }
//        for (AbstractShadowRenderer plsr : route.shadowRenderers) {
//             viewPort.addProcessor(plsr); // Disabled shadows for now
//        }
        for (LocationEvent newEvent : route.locEvents) {
            locEventBus.add(newEvent);
        }

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape(currentWorld);
        landscape = new RigidBodyControl(sceneShape, 0f);
        currentWorld.addControl(landscape);
        bulletAppState.getPhysicsSpace().add(landscape);

        // TODO freeze for a second
        playerControl.setPhysicsLocation(route.getStartLocs().get(entIndex));
        cam.lookAtDirection(route.getStartDirs().get(entIndex), Vector3f.UNIT_Y);
        // TODO other initializations

    }

    /**
     * Set up all the key mapping that we intend to use.
     * 
     */
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
        inputManager.deleteMapping(INPUT_MAPPING_EXIT); //TODO replace with pause
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_ESCAPE));

        inputManager.addListener(this, "Left");
        inputManager.addListener(this, "Right");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
        inputManager.addListener(this, "Interact");
        inputManager.addListener(this, "Jump");
        inputManager.addListener(this, "Pause");
    }

    /**
     * The main update loop.
     * 
     * This function is called once every frame of the game. :)
     * @param tpf 
     */
    @Override
    public void simpleUpdate(float tpf) {
//         if (!rootNode.descendantMatches("Models/Crate.blend").isEmpty()) {
//         Spatial spat = rootNode.descendantMatches("Models/Crate.blend").get(0);
//         System.out.println(spat.getName());
//         System.out.println(spat.getWorldTranslation().x);
//         System.out.println(spat.getWorldTranslation().y);
//         System.out.println(spat.getWorldTranslation().z);
//         System.out.println();
//         }
//        System.out.println(playerControl.getPhysicsLocation());
//        System.out.println(taskEventBus.get("door1"));
        if (!isPaused) {
            // Find direction of camera (and rotation)
            camDir.set(cam.getDirection().normalize());
            camLeft.set(cam.getLeft().normalize());
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
            playerControl.setWalkDirection(walkDirection.normalize().mult(25f * tpf));
            // Move camera to correspond to player
            cam.setLocation(playerControl.getPhysicsLocation().add(0, HALFCHARHEIGHT * 3 / 4, 0));

            
            // Position carried items appropriately
            if (draggedObject != null) {
                float distance = draggedObject.getSpatial().getLocalTranslation().length();
                Vector3f newLoc = camDir.mult(distance);
                draggedObject.getSpatial().setLocalTranslation(newLoc);
                draggedObject.getSpatial().setLocalRotation(cam.getRotation());
            }

            // Check character for collisions
            for (PhysicsCollisionObject object : billMurray.getOverlappingObjects()) {
                if (object instanceof RigidBodyControl) {
                    ((RigidBodyControl) object).applyCentralForce(camDir.mult(1000f));
                }
            }
            // Check global location event queue
            for (LocationEvent e : locEventBus) {
                // TODO cleanup
                e.checkAndFireEvent(this, playerControl.getPhysicsLocation());
//                if (e.checkCondition(playerControl.getPhysicsLocation())) {
//                    e.onDemoEvent(this);
//                }
            }
            // Update task queue
            ArrayDeque<ArrayDeque<DemoTask>> x = new ArrayDeque<>(taskEventBus.values());
            for (ArrayDeque<DemoTask> queue : x) {
                DemoTask task = queue.peek();
                task.updateTime(tpf);
                task.onTimeStep(tpf);
                if (task.isFinished()) {
//                    System.out.println("TaskType: " + task.getClass() + " from queue: " + task.getTaskQueueId() + " completed");
                    task.complete();
                    queue.pop();
                    if (queue.isEmpty()) {
                        taskEventBus.remove(task.getTaskQueueId());
                    }
                }
            }
        }
    }

    public void addTask(DemoTask task) {
        ArrayDeque<DemoTask> taskQueue = taskEventBus.get(task.getTaskQueueId());
        if (taskQueue != null) {
            taskQueue.add(task);
        } else {
            taskQueue = new ArrayDeque<>();
            taskQueue.add(task);
            taskEventBus.put(task.getTaskQueueId(), taskQueue);
        }
    }

    @Override
    public void onAction(String keyName, boolean isPressed, float tpf) {
        switch (keyName) {
        case "Left":
            keyLeft = isPressed;
            break;
        case "Right":
            keyRight = isPressed;
            break;
        case "Up":
            keyUp = isPressed;
            break;
        case "Down":
            keyDown = isPressed;
            break;
        case "Interact":
            if (isPressed) {
                if (gameScreen.isTextShowing() && gameScreen == nifty.getCurrentScreen().getScreenController()) {
                    gameScreen.progressThroughText();
                } else if (draggedObject != null) {
                    Spatial spatial = draggedObject.getSpatial();
                    // check object is not behind wall/floor
                    Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                    CollisionResults results = new CollisionResults();
                    rootNode.collideWith(ray, results);
                    
                    Boolean isCentreInside = true;
                    float distance = spatial.getLocalTranslation().subtract(cam.getLocation()).length();
                    if (spatial instanceof Geometry) {
                        for (CollisionResult collision: results) {
                            if (collision.getDistance() < distance && collision.getGeometry().equals((Geometry)spatial)) 
                                isCentreInside = false;
                        }
                    } else { // Currently only nodes are dragged
                        for (CollisionResult collision: results) {
                            if (collision.getDistance() < distance && !(collision.getGeometry().hasAncestor((Node) spatial))) 
                                isCentreInside = false;
                        }
                    }
                    
                    if (isCentreInside) {
                        // Drop current Object held
                        Vector3f location = spatial.getWorldTranslation();
                        bulletAppState.getPhysicsSpace().add(spatial);
                        spatial.removeFromParent();
                        rootNode.attachChild(spatial);
                        spatial.setLocalTranslation(location);
                        spatial.getControl(RigidBodyControl.class).setPhysicsLocation(location);
                        spatial.getControl(RigidBodyControl.class).activate();
                        draggedObject = null;
                    }
                } else {
                    // Ray Casting (checking for first interactable object)
                    Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                    CollisionResults results = new CollisionResults();
                    rootNode.collideWith(ray, results);
                    CollisionResult closest = results.getClosestCollision();
                    
                    // Gets the closest geometry (if it exists) and attempts to interact with it
                    if (closest != null && closest.getDistance() < 12f) {
                        System.out.println(closest.getGeometry().getName() + " found!");
                        if (!currentRoute.interactWith(this, closest.getGeometry())) {
                            System.out.println(closest.getGeometry().getName() + " is not responding...");
                        }
                    }
                }
            }
            break;
        case "Jump":
            if (isPressed) {
                playerControl.jump();
            }
            break;
        case "Pause":
            if (isPressed) {
                if (!isPaused) {
                    nifty.gotoScreen("pauseMenu");
                } else {
                    nifty.gotoScreen("game");
                }
            }
            break;
        }
    }

    public void pauseDemo() {
        if (!isPaused) {
        this.isPaused = true;
        bulletAppState.setEnabled(false);
        flyCam.setEnabled(false);}
    }

    public void unPauseDemo() {
        this.isPaused = false;
        bulletAppState.setEnabled(true);
        flyCam.setEnabled(true);
    }

    public void drag(Spatial spatial) {
        for(DemoObject object : currentRoute.objects) {
            if (object.getSpatial() == spatial)
                draggedObject = object;
        }
        // Remove it from the physics space
        bulletAppState.getPhysicsSpace().remove(spatial.getControl(RigidBodyControl.class));
        // Attach it to the player
        playerNode.attachChild(spatial);
    }

    public CharacterControl getPlayerControl() {
        return playerControl;
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
    
    public DialogueScreen getDialogueScreen() {
        return dialogueScreen;
    }

    @Override
    public AppStateManager getStateManager() {
        return stateManager;
    }

    public void chooseLocation(String routeName) {
        DemoRoute route = routes.get(routeName);
        if (route == null) {
            throw new RuntimeException("Error: No route found with name: " + routeName);
        }
        int entIndex = narrativeInstance.getRoute(gameScreen.getRoute()).getProperties().getInt("Entrance");
        loadRoute(route, entIndex);
        gameScreen.flushDialogueTextSequence();
        gameScreen.setDialogueTextSequence(route.startupTextSequence);
    }
    
//    public void flickerLights() {
//        for (DemoLight : currentRoute.lights) {
//            
//        }
//    }
}
