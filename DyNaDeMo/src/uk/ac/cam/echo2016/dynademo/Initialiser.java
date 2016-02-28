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
import com.oracle.xmlns.internal.webservices.jaxws_databinding.ExistingAnnotationsType;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.cam.echo2016.dynademo.screens.GameScreen;
import uk.ac.cam.echo2016.multinarrative.GraphElementNotFoundException;

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
    private static HashMap<String, DemoLight> lightMap;
    /**
     * @param app - the application to attach events and renderers to
     * @return
     */
    
    public static HashMap<String, DemoRoute> initialiseRoutes(MainApplication app) {
        final HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();
        addBedroomRoute(app, routes);
        addButtonRoute(app, routes);
        addDeathRoute(app, routes);
        addDoorLeftRoute(app, routes);
        addDoorRightRoute(app, routes);
        addEscapeRoute(app, routes);
        addLeverRoute(app, routes);
        addObservationRoute(app, routes);
        addPuzzleRoute(app, routes);
        return routes;
    }

    private static void addBedroomRoute(MainApplication app, HashMap<String, DemoRoute> routes) {        
        tRoute = new DemoRoute("BedroomRoute", "Scenes/BedroomRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        tLightNames = new String[]{
            "RoomLight1", "RoomLight2", "RoomLight3", "RoomLight4",
            "RoomLight5", "RoomLight6", "CorridorLight1", "CorridorLight2"};
        
        tLightCoords = new Vector3f[]{
            new Vector3f(0f, 6f, 0f), new Vector3f(25f, 6f, 0f), new Vector3f(25f, 6f, -30f),
            new Vector3f(0f, 6f, -30f), new Vector3f(-25f, 6f, -30f), new Vector3f(-25f, 6f, 0f),
            new Vector3f(25f, 6f, -15f), new Vector3f(-25f, 6f, -15f)};
        
        tLightAffected = new String[][]{
            {"Room1"}, {"Room2"}, {"Room3"}, {"Room4"},
            {"Room5"}, {"Room6"}, {"Corridor"}, {"Corridor"},};
        
        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);

        // EVENTS
        tLocEvent = new SyncPointEvent("Puzzle Or Observation", new BoundingBox(new Vector3f(-80, 1, -40), 40, 14, 50));
        tRoute.locEvents.add(tLocEvent);
        
        tRoute.startupTextSequence = new String[]{
            "Press 'e' on to scroll through this text...",
            "Pleas enjoy DyNaDeMo!"
        };
        
        routes.put(tRoute.getId(), tRoute);
    }
    
    private static void addButtonRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        InteractionEvent eInter;
        final ChoicePointEvent cpe;
        
        route = new DemoRoute("ButtonRoute", "Scenes/ButtonRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight1", "CorridorLight2"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,8,0), new Vector3f(-35,8,0), new Vector3f(35,8,0)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
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
        button.setLocalTranslation(0f,4f,-7f);
        button.setLocalRotation(new Quaternion().fromAngles(FastMath.PI/4, 0f,0f));
        button.move(new Vector3f(0f,1f,1f).normalize().mult(0.2f/(float)Math.sqrt(2f)));
        
        // object physics
        KinematicDemoObject buttonObj = new KinematicDemoObject("Button", button, 1f, true, null);
        buttonObj.getLights().add(lightMap.get("Room"));
        route.objects.add(buttonObj);
        
        // EVENTS
        cpe = new ChoicePointEvent("Third Select", new BoundingBox(new Vector3f(40,1,0), 10,14,5), "Button pressed", "Button not pressed");
        
        // object events
        eInter = new InteractionEvent("buttonInteraction", buttonObj) {
            public final static int DELAY = 1;
            public Vector3f displacement = new Vector3f(0f,1f,1f).normalize().mult(0.2f/(float)Math.sqrt(2f));
            @Override
            public void onDemoEvent(MainApplication app) {
                // TODO hack removal
                DemoRoute route = app.routes.get("ButtonRoute");
                KinematicDemoObject kinematicObj = (KinematicDemoObject) getObject();
                
                /*// TODO different property/affect?
                route.properties.putBoolean(getObject().getObjId(), true);*/
                cpe.setActionTaken(true);
                
                if (kinematicObj.getTasks().isEmpty()) {
                    getObject().getSpatial().move(displacement.negate());
                    kinematicObj.queueDelay(app, DELAY);
                    kinematicObj.queueDisplacement(app, 0.1f, displacement, displacement.length());
                }
            }
        };
        route.setInteractable(button, eInter);
        
        routes.put(route.getId(), route);
    }
   
    private static void addDeathRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route = new DemoRoute("DeathRoute", "Scenes/DeathRoute.j3o", new Vector3f(-30,0,0), 
                new Vector3f(1,0,0));
        
        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,8,0), new Vector3f(-25,8,0)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
        routes.put(route.getId(), route);
    }
    
    private static void addDoorLeftRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        
        DemoRoute route = new DemoRoute("DoorLeft", "Scenes/DoorLeft.j3o", new Vector3f(-30,0,0), new Vector3f(1,0,0));
        
        //LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight", "CorridorLeftLight"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,18,0), new Vector3f(-35,8,0), new Vector3f(0,8,-35)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor"}, {"CorridorLeft"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
        // EVENTS
        spe = new SyncPointEvent("Fate Decider", new BoundingBox(new Vector3f(0,1,-45),5,14,5));
        
        routes.put(route.getId(), route);
    }
    
    private static void addDoorRightRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        
        DemoRoute route = new DemoRoute("DoorRight", "Scenes/DoorRight.j3o", new Vector3f(-30,0,0), new Vector3f(1,0,0));
        
        //LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight", "CorridorRightLight"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,18,0), new Vector3f(-35,8,0), new Vector3f(0,8,35)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor"}, {"CorridorRight"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
        // EVENTS
        spe = new SyncPointEvent("Fate Decider", new BoundingBox(new Vector3f(0,1,45),5,14,5));
        
        routes.put(route.getId(), route);
    }
    
    private static void addEscapeRoute(MainApplication app, HashMap<String,DemoRoute> routes) {
        SyncPointEvent spe;
        
        DemoRoute route = new DemoRoute("EscapeRoute", "Scenes/EscapeRoute.j3o", new Vector3f(25,0,0), new Vector3f(-1,0,0));
        
        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight", "Corridor1Light", "Corridor2Light"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,8,0), new Vector3f(0,8,25), new Vector3f(-25,8,0), new Vector3f(25,8,0)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor"}, {"Corridor1"}, {"Corridor2"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
        // EVENTS
        spe = new SyncPointEvent("To Endings", new BoundingBox(new Vector3f(0, 1, 15), 5,14,5));
        
        routes.put(route.getId(), route);
    }
    
    private static void addLeverRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        InteractionEvent eInter;
        final ChoicePointEvent cpe;
        
        route = new DemoRoute("LeverRoute", "Scenes/LeverRoute.j3o", new Vector3f(-40, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));
        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(-10,8,0), new Vector3f(-35,8,0)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor"}
        };
        
        lightMap = addLights(app, route, tLightNames, tLightCoords, tLightAffected);

        // OBJECTS
        
        Spatial leverRoot = app.getAssetManager().loadModel("Models/Lever.j3o");
        leverRoot.setLocalTranslation(0f, 5f, 0f);
        leverRoot.setLocalRotation(new Quaternion().fromAngles(0, 0f, FastMath.PI/2));
        // WARNING: Rigid body applied after this transform - axis offset
        
        // hacky but it works :)
        Spatial leverRod = ((Node) leverRoot).descendantMatches("Lever").get(0);
        
        // object physics
        StaticDemoObject leverBaseObj = new StaticDemoObject("leverBase", leverRoot, true);
        leverBaseObj.getLights().add(lightMap.get("Room"));
        route.objects.add(leverBaseObj);
        
        // TODO bounding box actually required? see button
        KinematicDemoObject leverObj = new KinematicDemoObject("leverRod", leverRod, 1f, false, null);
        leverObj.getLights().add(lightMap.get("Room"));
        route.objects.add(leverObj);
        
        // EVENTS
        cpe = new ChoicePointEvent("LeverMovedLeft", new BoundingBox(new Vector3f(-45,1,0), 5,14,5), "Choose left", "Choose right");
        // object events
        route.properties.putInt(leverObj.getObjId(), 0);
        eInter = new LeverEvent("leverInteraction", leverObj, cpe);
        route.setInteractable(leverRoot, eInter);
        
        routes.put(route.getId(), route);
    }
    
    private static void addObservationRoute(MainApplication app, HashMap<String, DemoRoute> routes) {
        DemoRoute route;
        LocationEvent eLoc;

        route = new DemoRoute("ObservationRoute", "Scenes/ObservationRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        tLightNames = new String[] {"RoomLight", "CorridorLight1", "CorridorLight2"};
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0,8,0), new Vector3f(-5, 8, -30), new Vector3f(-30,8,-5)
        };
        
        tLightAffected = new String[][]{
            {"Room"}, {"Corridor1"}, {"Corridor2"}
        };
        
        addLights(app, route, tLightNames, tLightCoords, tLightAffected);
        
        // EVENTS
        eLoc = new SyncPointEvent("LeverOrButton", new BoundingBox(new Vector3f(-40,1,-5),5,14,5));
        route.locEvents.add(eLoc);
        
        routes.put(route.getId(), route);
    }
    
    private static void addPuzzleRoute(MainApplication app, final HashMap<String, DemoRoute> routes) {
        SyncPointEvent spe;
        ConditionalSyncPointEvent cspe1;
        ConditionalSyncPointEvent cspe2;
        ConditionalSyncPointEvent cspe3;
        BoundingBox bound;
        
        tRoute = new DemoRoute("PuzzleRoute", "Scenes/PuzzleRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, -45),
                new Vector3f(0, 0, 1));
        // LIGHTS
        tLightNames = new String[] {
            "RoomLight", "CorridorLight1", "CorridorLight2", "TallCorridorLight1", "TallCorridorLight2"
        };
        
        tLightCoords = new Vector3f[] {
            new Vector3f(0, 8f, 0), new Vector3f(0,6,-35), new Vector3f(0,6,35),
            new Vector3f(20,10,5), new Vector3f(40,10,5)
        };
        
        tLightAffected = new String[][] {
            {"Room"}, {"Corridor1"}, {"Corridor2"}, {"TallCorridor1"}, {"TallCorridor2"}
        };

        lightMap = addLights(app, tRoute, tLightNames, tLightCoords, tLightAffected);
        
        // Crate
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        bound = new BoundingBox(new Vector3f(0,0.75f,0), 1.5f, 1.5f, 1.5f);
        crate.setLocalTranslation(0, 0, -30);
        
        // object physics
        DynamicDemoObject crateObj = new DynamicDemoObject("crate", crate, 5f, true, bound);
        crateObj.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(crateObj);
        
        // object events
        tInterEvent = new InteractionEvent("crateInteraction", crateObj){
            @Override
            public void onDemoEvent(MainApplication app) {
                app.drag(getObject().getSpatial());
            }
        };
        tRoute.setInteractable(crate, tInterEvent);
        
        // PressurePlate
        Spatial pressPlate1 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        Spatial pressPlate2 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        bound = new BoundingBox(new Vector3f(0,0.4f,0), 1.5f, 0.4f, 1.5f);
        
        pressPlate1.setLocalTranslation(-5f, 0, -5f);
        pressPlate2.setLocalTranslation(-5f, 0, 5f);
        
        KinematicDemoObject plateObj1 = new KinematicDemoObject("pressurePlate1", pressPlate1, 1f, true, bound);
        bound = new BoundingBox(new Vector3f(0,0.4f,0), 1.5f, 0.4f, 1.5f);
        KinematicDemoObject plateObj2 = new KinematicDemoObject("pressurePlate2", pressPlate2, 1f, true, bound);
        plateObj1.getLights().add(lightMap.get("RoomLight"));
        plateObj2.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(plateObj1);
        tRoute.objects.add(plateObj2);
        
        tRoute.properties.putBoolean(plateObj1.getObjId(), false);
        tRoute.properties.putBoolean(plateObj2.getObjId(), false);
        
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, -5f), 1.3f, 0.4f, 1.3f);
//        eLoc = new PressurePlateEvent("pressurePlate1", new Vector3f(-6.5f, 0f, 3.5f),  3.2f, 0.8f + HALFCHARHEIGHT, 3.2f, plateObj1);
        tLocEvent = new PressurePlateEvent("pressurePlate1", bound, plateObj1) {

            @Override
            public void onPressed() {
                System.out.println("Hi");
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onRelease() {
                System.out.println("Bye");
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
        // TODO bad code
        ((PressurePlateEvent)tLocEvent).activators.add(crateObj);
        tRoute.locEvents.add(tLocEvent);
        bound = new BoundingBox(new Vector3f(-5f, 0.4f, 5f), 1.3f, 0.4f, 1.3f);
//        eLoc = new PressurePlateEvent("pressurePlate2", new Vector3f(-6.5f, 0f, -6.5f), 3.2f, 0.8f + HALFCHARHEIGHT, 3.2f, plateObj2);
        tLocEvent = new PressurePlateEvent("pressurePlate2", bound, plateObj2) {
            
            @Override
            public void onPressed() {
                System.out.println("Hi");
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void onRelease() {
                System.out.println("Bye");
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
        ((PressurePlateEvent)tLocEvent).activators.add(crateObj);
        tRoute.locEvents.add(tLocEvent);
         
        tRoute.startupTextSequence = new String[] {
            "Press 'e' to interact with objects."
        };
        
        Spatial door1 = extractDoor(app, 0);
        
        door1.setLocalTranslation(10f, 0f, 2.5f);
        KinematicDemoObject doorObj1 = new KinematicDemoObject(("door1"), door1, 1f, true, null);
        doorObj1.getLights().add(lightMap.get("RoomLight"));
        tRoute.objects.add(doorObj1);
        
        // EVENTS
        spe = new SyncPointEvent("PuzzleSolvedExit", new BoundingBox(new Vector3f(45,1,5),5,14,5));
        cspe1 = new ConditionalSyncPointEvent("FirstExitEvent", new BoundingBox(new Vector3f(0,1,45),5,14,5), "See puzzle first time");
        cspe2 = new ConditionalSyncPointEvent("PuzzleUnsolvedEvent", new BoundingBox(new Vector3f(0,1,-45),5,14,5), "Puzzle again");
        cspe3 = new ConditionalSyncPointEvent("PuzzleUnsolvedEvent", new BoundingBox(new Vector3f(0,1,-45),5,14,5), "Puzzle unsolvable");
        
        routes.put(tRoute.getId(), tRoute);
    }
    
    private static DemoLight addLight(MainApplication app, DemoRoute route, Vector3f loc, String[] spatialNames) {
        PointLight l = new PointLight();
        l.setColor(ColorRGBA.Gray);
        l.setPosition(loc);
        l.setRadius(1000f);
        
        DemoLight dLight =  new DemoLight(l, spatialNames);
        route.lights.add(dLight);

        PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
        plsr.setLight(l);
        plsr.setFlushQueues(false);
        plsr.setShadowIntensity(0.1f);
        route.shadowRenderers.add(plsr);
        
        return dLight;
    }
    
    private static HashMap<String, DemoLight> addLights(MainApplication app, DemoRoute route, String[] lightNames, Vector3f[] lightCoords, String[][] spatialNames) {
        HashMap<String, DemoLight> result = new HashMap<>();
        
        if (lightNames.length != lightCoords.length || lightNames.length != spatialNames.length)
            throw new RuntimeException("Error: incorrect number of light properties");
        
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
    private static Spatial extractDoor(MainApplication app, int numberOfHandles) {
        Spatial doors = app.getAssetManager().loadModel("Models/Doors.j3o");
        switch (numberOfHandles) {
        case 0: return ((Node) doors).descendantMatches("BlankDoor").get(0);
        case 1: return ((Node) doors).descendantMatches("BlankDoor").get(0);
        case 2: return ((Node) doors).descendantMatches("BlankDoor").get(0);
        default: throw new RuntimeException("Too many door handles");
        }
    }
    
    private static class LeverEvent extends InteractionEvent {
        private int leverCount;
        private ChoicePointEvent CPE;

        public LeverEvent(String id, DemoObject object, ChoicePointEvent cpe) {
            super(id, object);
            CPE = cpe;
        }

        @Override
        public void onDemoEvent(MainApplication app) {
            DemoRoute leverRoute = app.routes.get("LeverRoute");
//            int leverCount = leverRoute.properties.getInt(getObject().getObjId());
            KinematicDemoObject kinematicObj = (KinematicDemoObject) getObject();
            if (leverCount < 10) {
                CPE.setActionTaken(!CPE.getActionTaken());
                if (leverCount % 2 == 0) {
                    kinematicObj.queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), -FastMath.PI / 2);
                } else {
                    kinematicObj.queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), FastMath.PI / 2);
                }
            } else {
                app.getGameScreen().setDialogueTextSequence(new String[]{"You broke it. Well done."});
            }
            ++leverCount;
            leverRoute.properties.putInt(getObject().getObjId(), leverCount);
        }
    };
    
    private abstract static class PressurePlateEvent extends ProximityEvent {
        public final static int DELAY = 1;
        
        public PressurePlateEvent(String id, BoundingBox bound, DemoObject object) {
            super(id, bound, object);
        }
        public abstract void onPressed();
        public abstract void onRelease();
        
        @Override
        public void onDemoEvent(MainApplication app) {
            DemoRoute route = app.routes.get("PuzzleRoute");
            if (!(route.properties.containsKey(object.getObjId()))) {
                throw new RuntimeException("Error: Property not found.");
            }
            Boolean plateDown = route.properties.getBoolean(object.getObjId());
            // TODO again hacky like leverRod mesh
            KinematicDemoObject kinematicObj = (KinematicDemoObject) object;
            if (!plateDown) {
                object.getSpatial().move(0, -0.75f, 0);
                route.properties.putBoolean(object.getObjId(), true);
                
                kinematicObj.queueDelay(app, DELAY);
                kinematicObj.queueDisplacement(app, 0.1f, Vector3f.UNIT_Y, 0.75f);
                kinematicObj.queueProperty(app, 0.0f, route.properties, object.getObjId(), false);
                onPressed();
                app.addTask(new DemoTask(object.getObjId(), 0f){
                    @Override
                    public void complete() {
                        onRelease();
                    }
                });
            }
            if (kinematicObj.getTasks().isEmpty()) {
                throw new RuntimeException("Error: Illegal pressure plate state for: " + object.getObjId());
            } else {
                DemoTask currentTask = kinematicObj.getTasks().getFirst();
                if (currentTask instanceof KinematicTask) {
                    currentTask.resetTime();
                } else if (currentTask instanceof TranslationTask) {
                    kinematicObj.getTasks().remove(currentTask);
                    float x = (-0.75f * currentTask.getCurrentTime() / currentTask.getCompletionTime());
                    object.getSpatial().move(0, x, 0);
                } else if (currentTask instanceof AddPropertyTask) {
                    kinematicObj.getTasks().remove(currentTask);
                }
            }
        }
        
    };
    
    private static class ChoicePointEvent extends LocationEvent {
        private boolean actionTaken = false;
        private String routeIfTrue;
        private String routeIfFalse;
        
        public ChoicePointEvent(String id, BoundingBox bound, String RouteIfActionTaken, String RouteOtherwise) {
            super(id, bound);
            routeIfTrue = RouteIfActionTaken;
            routeIfFalse = RouteOtherwise;
        }
        
        public boolean getActionTaken() { return actionTaken; }
        public void setActionTaken(boolean isAction) {
            actionTaken = isAction;
        }
        
        @Override
        public void onDemoEvent(MainApplication app) {
            try {
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
            if (app.getGameScreen().getRoute() == correctRoute) {
                super.onDemoEvent(app);
            }
        }
    }
}
