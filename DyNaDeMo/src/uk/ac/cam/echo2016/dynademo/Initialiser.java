package uk.ac.cam.echo2016.dynademo;

import com.jme3.bounding.BoundingBox;
import static uk.ac.cam.echo2016.dynademo.MainApplication.HALFCHARHEIGHT;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.multinarrative.GameChoice;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;
import uk.ac.cam.echo2016.multinarrative.Route;

/**
 * @author tr393
 */
public class Initialiser {

    private static DemoRoute tRoute;
    private static LocationEvent tLocEvent;
    private static InteractionEvent tInterEvent;
    private static String[] tLightNames;
    private static Vector3f[] tLightCoords;
    private static String[][] tLightAffected;
    private static ArrayList<Vector3f> locList= new ArrayList<Vector3f>();
    private static ArrayList<Vector3f> dirList= new ArrayList<Vector3f>();
    private static HashMap<String, DemoLight> lightMap;

    /**
     * @param app - the application to attach events and renderers to
     * @return
     */
    public static HashMap<String, DemoRoute> initialiseRoutes(MainApplication app) {
        final HashMap<String, DemoRoute> routes = new HashMap<>();
        addBedroomRoute(app, routes);
        addButtonRoute(app, routes);
        addChar1DeathRoute(app, routes);
        addChar2DeathRoute(app, routes);
        addDoorLeftRoute(app, routes);
        addDoorRightRoute(app, routes);
        addEscapeRoute(app, routes);
        addLeverRoute(app, routes);
        addObservationRoute(app, routes);
        addPuzzleRoute(app, routes);
        return routes;
    }

    private static void addBedroomRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0));
        locList.add(new Vector3f(-65, HALFCHARHEIGHT + 1.0f,-15));
        
        dirList.clear();
        dirList.add(new Vector3f(1,0,0));
        dirList.add(new Vector3f(-1,0,0));
        tRoute = new DemoRoute("BedroomRoute", "Scenes/BedroomRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{
            "RoomLight1", "RoomLight2", "RoomLight3", "RoomLight4",
            "RoomLight5", "RoomLight6", "CorridorLight1", "CorridorLight2",
            "MeetingRoomLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0f, 6f, 0f), new Vector3f(25f, 6f, 0f), new Vector3f(25f, 6f, -30f),
            new Vector3f(0f, 6f, -30f), new Vector3f(-25f, 6f, -30f), new Vector3f(-25f, 6f, 0f),
            new Vector3f(25f, 6f, -15f), new Vector3f(-25f, 6f, -15f), new Vector3f(-70,8,-15)};

        tLightAffected = new String[][]{
            {"Room1"}, {"Room2"}, {"Room3"}, {"Room4"},
            {"Room5"}, {"Room6"}, {"Corridor"}, {"Corridor"},{"MeetingRoom"}};

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tLocEvent = new SyncPointEvent("Puzzle Or Observation", new BoundingBox(new Vector3f(-70, 1, -15), 5, 14, 5));
        tRoute.locEvents.add(tLocEvent);

        tRoute.startupTextSequence = new String[]{
            "Press 'e' on to scroll through this text...",
            "Pleas enjoy DyNaDeMo!"
        };

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addButtonRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        InteractionEvent eInter;
        final ChoiceThenSyncPointEvent cpe;
        locList.clear();
        locList.add(new Vector3f(-40, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));
                

        tRoute = new DemoRoute("ButtonRoute", "Scenes/ButtonRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight1", "CorridorLight2"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(-35, 8, 0), new Vector3f(35, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS

//        Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
//        lever.setLocalTranslation(0f, 5f, 10f);
//        lever.setLocalRotation(new Quaternion().fromAngles(-FastMath.PI/2, 0f,0f));
//        // WARNING: Rigid body applied after this transform - axis offset
//        
//        // hacky but it works :)
//        Spatial leverRod = ((Node) lever).descendantMatches("Lever").get(0);
//        
//        // object physics
//        StaticDemoObject leverBaseObj = new StaticDemoObject("LeverBase", lever, true);
//        leverBaseObj.getLights().add(light);
//        route.objects.add(leverBaseObj);
//        
//        KinematicDemoObject leverObj = new KinematicDemoObject("leverRod", leverRod, 1f, false);
//        leverObj.getLights().add(light);
//        route.objects.add(leverObj);
//        
//        // object events
//        route.properties.putInt(leverObj.getObjId(), 0);
//        eInter = new LeverEvent("lever", leverObj);
//        route.setInteractable(lever, eInter);


        Spatial button = app.getAssetManager().loadModel("Models/Button.j3o");
        button.setLocalTranslation(0f, 4f, -7f);
        button.setLocalRotation(new Quaternion().fromAngles(FastMath.PI / 4, 0f, 0f));
        button.move(new Vector3f(0f, 1f, 1f).normalize().mult(0.2f / (float) Math.sqrt(2f)));

        // object physics
        ButtonObject buttonObj = new ButtonObject("button", button, 1f, true, null);
        buttonObj.getLights().add(lightMap.get("RoomLight"));

        // EVENTS
        cpe =
                new ChoiceThenSyncPointEvent("Third Select", new BoundingBox(new Vector3f(40, 1, 0), 10, 14, 5), "Button pressed", "Button not pressed");
        tRoute.locEvents.add(cpe);

        eInter = new InteractionEvent("buttonInteraction", buttonObj);
        tRoute.setInteractable(button, eInter);

        tRoute.objects.add(buttonObj);
        routes.put(tRoute.getId(), tRoute);
    }

    private static void addChar1DeathRoute( final MainApplication app, final HashMap<String, DemoRoute> routes) {
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 5));
        dirList.clear();
        dirList.add(new Vector3f(0, 0, -1));
        
        tRoute = new DemoRoute("Char1DeathRoute", "Scenes/Char1DeathRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"MeetingRoomLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0)
        };

        tLightAffected = new String[][]{
            {"MeetingRoom"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addChar2DeathRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        locList.clear();
        locList.add(new Vector3f(-30, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));
        
        tRoute = new DemoRoute("Char2DeathRoute", "Scenes/Char2DeathRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(-25, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addDoorLeftRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        locList.clear();
        locList.add(new Vector3f(-30, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));

        tRoute = new DemoRoute("DoorLeft", "Scenes/DoorLeftRoute.j3o", locList, dirList);

        //LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight", "CorridorLeftLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 18, 0), new Vector3f(-35, 8, 0), new Vector3f(0, 8, -35)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}, {"CorridorLeft"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        spe = new SyncPointEvent("Fate Decider", new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5));
        tRoute.locEvents.add(spe);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addDoorRightRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        locList.clear();
        locList.add(new Vector3f(-30, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));

        tRoute = new DemoRoute("DoorRight", "Scenes/DoorRightRoute.j3o", locList, dirList);

        //LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight", "CorridorRightLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 18, 0), new Vector3f(-35, 8, 0), new Vector3f(0, 8, 35)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}, {"CorridorRight"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        spe = new SyncPointEvent("Fate Decider", new BoundingBox(new Vector3f(0, 1, 45), 5, 14, 5));
        tRoute.locEvents.add(spe);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addEscapeRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        locList.clear();
        locList.add(new Vector3f(25, HALFCHARHEIGHT + 1.0f, 0));
        locList.add(new Vector3f(-25, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(-1, 0, 0));
        dirList.add(new Vector3f(1,0,0));
                

        tRoute = new DemoRoute("EscapeRoute", "Scenes/EscapeRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight", "Corridor1Light", "Corridor2Light"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(0, 8, 25), new Vector3f(-25, 8, 0), new Vector3f(25, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}, {"Corridor1"}, {"Corridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        spe = new SyncPointEvent("To Endings", new BoundingBox(new Vector3f(0, 1, 15), 5, 14, 5));
        tRoute.locEvents.add(spe);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addLeverRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        InteractionEvent eInter;
        final ChoiceThenSyncPointEvent cpe;
        locList.clear();
        locList.add(new Vector3f(-35, HALFCHARHEIGHT + 1.0f, 0));
        dirList.clear();
        dirList.add(new Vector3f(1, 0, 0));

        tRoute = new DemoRoute("LeverRoute", "Scenes/LeverRoute.j3o", locList, dirList);
        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight"};

        tLightCoords = new Vector3f[]{
            new Vector3f(-10, 8, 0), new Vector3f(-35, 8, 0)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS

        Spatial leverRoot = app.getAssetManager().loadModel("Models/Lever.j3o");
        leverRoot.setLocalTranslation(0f, 5f, 0f);
        leverRoot.setLocalRotation(new Quaternion().fromAngles(0, 0f, FastMath.PI / 2));
        // WARNING: Rigid body applied after this transform - axis offset

        // hacky but it works :)
        Spatial leverRod = ((Node) leverRoot).descendantMatches("Lever").get(0);

        // object physics
        StaticDemoObject leverBaseObj = new StaticDemoObject("leverBase", leverRoot, true);
        leverBaseObj.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(leverBaseObj);

        cpe =
                new ChoiceThenSyncPointEvent("LeverMovedLeft", new BoundingBox(new Vector3f(-45, 1, 0), 5, 14, 5), "Choose right", "Choose left");

        // TODO bounding box actually required? see button
        LeverObject leverObj = new LeverObject("leverRod", leverRod, 1f, false, null, cpe);
        leverObj.getLights().add(lightMap.get("RoomLight"));

        // EVENTS
        tRoute.locEvents.add(cpe);

        // object events
        tRoute.properties.putInt(leverObj.getObjId(), 0);
        eInter = new InteractionEvent("leverInteraction", leverObj);
        tRoute.setInteractable(leverRoot, eInter);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addObservationRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        LocationEvent eLoc;
        locList.clear();
        locList.add(new Vector3f(-5, HALFCHARHEIGHT + 1.0f, -30));
        dirList.clear();
        dirList.add(new Vector3f(0, 0, 1));

        tRoute = new DemoRoute("ObservationRoute", "Scenes/ObservationRoute.j3o", locList, dirList);

        // LIGHTS
        tLightNames = new String[]{"RoomLight", "CorridorLight1", "CorridorLight2"};

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8, 0), new Vector3f(-5, 8, -30), new Vector3f(-30, 8, -5)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };

        addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        eLoc = new SyncPointEvent("LeverOrButton", new BoundingBox(new Vector3f(-40, 1, -5), 5, 14, 5));
        tRoute.locEvents.add(eLoc);

        routes.put(tRoute.getId(), tRoute);
    }

    private static void addPuzzleRoute(final MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        ConditionalSyncPointEvent cspe1;
        ConditionalSyncPointEvent cspe2;
        ConditionalSyncPointEvent cspe3;
        BoundingBox bound;
        locList.clear();
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, -45));
        locList.add(new Vector3f(0, HALFCHARHEIGHT + 1.0f, 45));
        dirList.clear();
        dirList.add(new Vector3f(0, 0, 1));
        dirList.add(new Vector3f(0, 0, -1));

        tRoute = new DemoRoute("PuzzleRoute", "Scenes/PuzzleRoute.j3o", locList, dirList);
        // LIGHTS
        tLightNames = new String[]{
            "RoomLight", "CorridorLight1", "CorridorLight2", "TallCorridorLight1", "TallCorridorLight2"
        };

        tLightCoords = new Vector3f[]{
            new Vector3f(0, 8f, 0), new Vector3f(0, 6, -35), new Vector3f(0, 6, 35),
            new Vector3f(20, 10, 5), new Vector3f(40, 10, 5)
        };

        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}, {"TallCorridor1"}, {"TallCorridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS
        // Crate
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        bound = new BoundingBox(new Vector3f(0, 0.75f, 0), 1.5f, 1.5f, 1.5f);
        crate.setLocalTranslation(0, 0, -30);

        // object physics
        CrateObject crateObj = new CrateObject("crate", crate, 5f, true, bound);
        crateObj.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(crateObj);

        // object events
        tInterEvent = new InteractionEvent("crateInteraction", crateObj);
        tRoute.setInteractable(crate, tInterEvent);

        // Door
        Spatial door1 = extractDoor(app, 0);

        door1.setLocalTranslation(10f, 0f, 2.5f);
        final DoorObject doorObj1 = new DoorObject("door1", door1, 1f, true, null);
        doorObj1.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(doorObj1);

        // PressurePlate
        Spatial pressPlate1 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        Spatial pressPlate2 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        bound = new BoundingBox(new Vector3f(0, 0.4f, 0), 1.5f, 0.4f, 1.5f);

        pressPlate1.setLocalTranslation(-5f, 0, -5f);
        pressPlate2.setLocalTranslation(-5f, 0, 5f);

        final PressurePlateObject plateObj1 = new PressurePlateObject("pressurePlate1", pressPlate1, 1f, true, bound) {
            @Override
            public void onPressed() {
                doorObj1.open(app);
            }
            @Override
            public void onRelease() {
                doorObj1.close(app);
            }
        };
        bound = new BoundingBox(new Vector3f(0, 0.4f, 0), 1.5f, 0.4f, 1.5f) {
            
        };
        final PressurePlateObject plateObj2 = new PressurePlateObject("pressurePlate2", pressPlate2, 1f, true, bound) {

            @Override
            public void onPressed() {
                System.out.println("Hi");
            }

            @Override
            public void onRelease() {
                System.out.println("Bye");
            }
        };
        plateObj1.getLights().add(lightMap.get("RoomLight"));
        plateObj2.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(plateObj1);
        tRoute.objects.add(plateObj2);

        tRoute.properties.putBoolean(plateObj1.getObjId(), false);
        tRoute.properties.putBoolean(plateObj2.getObjId(), false);

        bound = new BoundingBox(new Vector3f(-5f, 0.4f, -5f), 1.3f, 0.4f, 1.3f);
        
        tLocEvent = new ProximityEvent("pressurePlate1", bound, plateObj1) {
            @Override
            public void onDemoEvent(MainApplication app) {
                plateObj1.activate(app);
            }
        };

        ((ProximityEvent) tLocEvent).activators.add(crateObj);
        tRoute.locEvents.add(tLocEvent);
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, 5f), 1.3f, 0.4f, 1.3f);
        
        tLocEvent = new ProximityEvent("pressurePlate2", bound, plateObj2) {
            @Override
            public void onDemoEvent(MainApplication app) {
                plateObj2.activate(app);
            }
        };

        ((ProximityEvent) tLocEvent).activators.add(crateObj);
        tRoute.locEvents.add(tLocEvent);

        tRoute.startupTextSequence = new String[]{
            "Press 'e' to interact with objects."
        };

        // EVENTS
        spe = new SyncPointEvent("PuzzleSolvedExit", new BoundingBox(new Vector3f(45, 1, 5), 5, 14, 5));
        cspe1 =
                new ConditionalSyncPointEvent("FirstExitEvent", new BoundingBox(new Vector3f(0, 1, 45), 5, 14, 5), "See puzzle first time");
        cspe2 =
                new ConditionalSyncPointEvent("PuzzleUnsolvedEvent", new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5), "Puzzle again");
        cspe3 =
                new ConditionalSyncPointEvent("PuzzleUnsolvableEvent", new BoundingBox(new Vector3f(0, 1, -45), 5, 14, 5), "Puzzle unsolvable");
        tRoute.locEvents.add(spe);
        tRoute.locEvents.add(cspe1);
        tRoute.locEvents.add(cspe2);
        tRoute.locEvents.add(cspe3);

        routes.put(tRoute.getId(), tRoute);
    }

    private static DemoLight addLight(final MainApplication app, DemoRoute route, Vector3f loc, String[] spatialNames) {
        PointLight l = new PointLight();
        l.setColor(ColorRGBA.Gray);
        l.setPosition(loc);
        l.setRadius(1000f);

        DemoLight dLight = new DemoLight(l, spatialNames);
        route.lights.add(dLight);

        PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
        plsr.setLight(l);
        plsr.setFlushQueues(false);
        plsr.setShadowIntensity(0.1f);
        route.shadowRenderers.add(plsr);

        return dLight;
    }

    private static HashMap<String, DemoLight> addLights(final MainApplication app, DemoRoute route, String[] lightNames, Vector3f[] lightCoords, String[][] spatialNames) {
        HashMap<String, DemoLight> result = new HashMap<>();

        if (lightNames.length != lightCoords.length || lightNames.length != spatialNames.length) {
            throw new RuntimeException("Error: incorrect number of light properties");
        }

        for (int i = 0; i < spatialNames.length; ++i) {
            result.put(lightNames[i], addLight(app, route, lightCoords[i], spatialNames[i]));
        }
        return result;
    }

    /**
     *
     * @param type - 0
     * @return
     */
    private static Spatial extractDoor(final MainApplication app, int numberOfHandles) {
        Spatial doors = app.getAssetManager().loadModel("Models/Doors.j3o");
        switch (numberOfHandles) {
        case 0:
            return ((Node) doors).descendantMatches("BlankDoor").get(0);
        case 1:
            return ((Node) doors).descendantMatches("BlankDoor").get(0);
        case 2:
            return ((Node) doors).descendantMatches("BlankDoor").get(0);
        default:
            throw new RuntimeException("Too many door handles");
        }
    }

    /**
     * As in this game, all choicepoints are immediatlely followed by a SyncPoint from a game-play perspective, this
     * represents a combination of both.
     * 
     * 
     */
    public static class ChoiceThenSyncPointEvent extends LocationEvent {

        private boolean actionTaken = false;
        private String routeIfTrue;
        private String routeIfFalse;

        public ChoiceThenSyncPointEvent(String id, BoundingBox bound, String RouteIfActionTaken, String RouteOtherwise) {
            super(id, bound);
            routeIfTrue = RouteIfActionTaken;
            routeIfFalse = RouteOtherwise;
        }

        public boolean getActionTaken() {
            return actionTaken;
        }

        public void setActionTaken(boolean isAction) {
            actionTaken = isAction;
            System.out.println("actionTaken is now: " + actionTaken);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            try {
                app.getNarrativeInstance().startRoute(app.getGameScreen().getRoute());
                GameChoice gameChoice = app.getNarrativeInstance().endRoute(app.getGameScreen().getRoute());
                
                List<Route> options = gameChoice.getOptions();
                boolean routeIfTrueFromOptions = false;
                boolean routeIfFalseFromOptions = false;
                
                //Iterate through all the options and see if they match up with the routes we think they should be
                for(Route option: options) {
                    if(option.getId().equals(routeIfTrue)) {
                        routeIfTrueFromOptions = true;
                    } else if(option.getId().equals(routeIfFalse)) {
                        routeIfFalseFromOptions = true;
                    }
                }
                if(!routeIfTrueFromOptions || !routeIfFalseFromOptions)
                    System.out.println("The choices available don't match the routes that we think are avaialble.");
                if (actionTaken) {
                    app.getNarrativeInstance().startRoute(routeIfTrue);
                    app.getNarrativeInstance().endRoute(routeIfTrue);
                } else {
                    app.getNarrativeInstance().startRoute(routeIfFalse);
                    app.getNarrativeInstance().endRoute(routeIfFalse);
                }
            } catch (GraphElementNotFoundException ex) {
                Logger.getLogger(Initialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.nifty.gotoScreen("characterSelect");
        }
    }

    private static class SyncPointEvent extends LocationEvent {

        public SyncPointEvent(String id, BoundingBox bound) {
            super(id, bound);
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            try {
                //Ending the route that was started to show the correct character select screen to the player
                app.getNarrativeInstance().startRoute(app.getGameScreen().getRoute());
                app.getNarrativeInstance().endRoute(app.getGameScreen().getRoute());
            } catch (GraphElementNotFoundException ex) {
                Logger.getLogger(Initialiser.class.getName()).log(Level.SEVERE, null, ex);
            }
            app.nifty.gotoScreen("characterSelect");
        }
    }

    private static class ConditionalSyncPointEvent extends SyncPointEvent {

        private String correctRoute;

        public ConditionalSyncPointEvent(String id, BoundingBox bound, String theCorrectRoute) {
            super(id, bound);
            correctRoute = theCorrectRoute;
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            if (app.getGameScreen().getRoute().equals(correctRoute)) {
                super.onDemoEvent(app);
            }
        }
    }
}
