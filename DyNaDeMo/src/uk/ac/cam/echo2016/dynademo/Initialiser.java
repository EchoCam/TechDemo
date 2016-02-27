package uk.ac.cam.echo2016.dynademo;

import com.jme3.bullet.control.RigidBodyControl;

import static uk.ac.cam.echo2016.dynademo.MainApplication.HALFCHARHEIGHT;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.shadow.PointLightShadowRenderer;

import java.util.HashMap;

/**
 * @author tr393
 */
public class Initialiser {

    /**
     * 
     * @param app
     *            - required for event subscribing and renderer attaching
     * @return
     */
    public static HashMap<String, DemoRoute> initialiseRoutes(MainApplication app) {
        final HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();

        DemoRoute route;
        LocationEvent eLoc;
        InteractionEvent eInter;
        DemoLight light;

        //****** Bedroom ******//
        route = new DemoRoute("BedroomRoute", "Scenes/BedroomRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        Vector3f[] lightCoords = { new Vector3f(0f, 6f, 0f), new Vector3f(25f, 6f, 0f), new Vector3f(25f, 6f, -30f),
                new Vector3f(0f, 6f, -30f), new Vector3f(-25f, 6f, -30f), new Vector3f(-25f, 6f, 0f),

                new Vector3f(25f, 6f, -15f), new Vector3f(-25f, 6f, -15f),
        //
        // new Vector3f(-60f,6f,0f),
        // new Vector3f(-60f,6f,-30f)
        };
        String[][] spatialNames = { { "Room1" },// ,"Corridor"},
                { "Room2" },// ,"Corridor"},
                { "Room3" },// ,"Corridor"},
                { "Room4" },// ,"Corridor"},
                { "Room5" },// ,"Corridor"},
                { "Room6" },// ,"Corridor"}
                { "Corridor" }, { "Corridor" }, };
        for (int i = 0; i < spatialNames.length; ++i) {
            addLight(app, route, lightCoords[i], spatialNames[i]);
        }
        // EVENTS
        eLoc = new LocationEvent("Node1", new Vector3f(-80, 1, -40), 40, 14, 50) {

            @Override
            public void onDemoEvent(MainApplication app) {
                //TODO first meeting
                //could do...

                //routes.get(gameScreen.getRouteName());
                //to get the name of the route the player has selected
                app.loadRoute(app.routes.get("PuzzleRoute")); // temp functionality
                app.getGameScreen().setDialogueTextSequence(new String[]{"You are now in the puzzle room"});
            }
            
        };
        route.locEvents.add(eLoc);
        routes.put(route.getId(), route);

        //****** Puzzle Room ******//
        route = new DemoRoute("PuzzleRoute", "Scenes/PuzzleRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, -45),
                new Vector3f(0, 0, 1));
        // LIGHTS
        light = addLight(app, route, new Vector3f(0, 8f, 0), new String[] {"Room"});
        addLight(app, route, new Vector3f(0,6,-35), new String[] {"Corridor1"});
        addLight(app, route, new Vector3f(0,6,35), new String[] {"Corridor2"});
        addLight(app, route, new Vector3f(20,10,5), new String[] {"TallCorridor1"});
        addLight(app, route, new Vector3f(40,10,5), new String[] {"TallCorridor2"});

        // Crate
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        crate.setLocalTranslation(0, 0, -30);
        // object physics
        DynamicDemoObject crateObj = new DynamicDemoObject(crate, 5f, true);
        crateObj.lights.add(light);
        route.objects.add(crateObj);
        // object events
        eInter = new InteractionEvent("crate", crateObj){
            @Override
            public void onDemoEvent(MainApplication app) {
                app.drag(getObject().spatial);
            }
        };
        route.setInteractable(crate, eInter);
        
        // PressurePlate
        Spatial pressPlate1 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        Spatial pressPlate2 = app.getAssetManager().loadModel("Models/PressurePlate.j3o");
        pressPlate1.setLocalTranslation(-5f, 0.1f, 5f);
        pressPlate2.setLocalTranslation(-5f, 0.1f, -5f);
        KinematicDemoObject plateObj1 = new KinematicDemoObject(pressPlate1, 1f, true);
        KinematicDemoObject plateObj2 = new KinematicDemoObject(pressPlate2, 1f, true);
        plateObj1.lights.add(light);
        plateObj2.lights.add(light);
        route.objects.add(plateObj1);
        route.objects.add(plateObj2);
        
//        Box bMesh = new Box(new Vector3f(-5,1.6f,5), 1.5f, 1.5f, 1.5f);
//        Geometry bGeom = new Geometry("box", bMesh);
//        Material bMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
//        bGeom.setMaterial(bMat);
//        app.getRootNode().attachChild(bGeom);
        
//        eLoc = new DemoProximityEvent("pressurePlate1", new Vector3f(-6.5f, 0.1f, 3.5f), 3f, 0.8f + HALFCHARHEIGHT, 3f, plateObj1);
        
        route.properties.putBoolean(pressPlate1.getName(), false);
        route.properties.putBoolean(pressPlate1.getName(), false);
        class PressurePlateEvent extends ProximityEvent {
            public PressurePlateEvent(String id, Vector3f loc, float width, float height, float depth, DemoObject object) {
                super(id, loc, width, height, depth, object);
            }
            @Override
            public void onDemoEvent(MainApplication app) {
                if (app.getPlayerControl().onGround()) {
                    // TODO - improve similar to levers
                    DemoRoute route = routes.get("PuzzleRoute");
                    Boolean plateDown = route.properties.getBoolean(object.spatial.getName());
                    // TODO again hacky like leverRod mesh
                    if (!plateDown) {
                        object.spatial.move(0, -0.75f, 0);
                        route.properties.putBoolean(getId(), true);
                    }
                    KinematicDemoObject kinematicObj = (KinematicDemoObject) object;
                    kinematicObj.queueDelay(app, 1f);
                    kinematicObj.queueDisplacement(app, 0.1f, Vector3f.UNIT_Y, 0.75f);
                }
            }
        };
        eLoc = new PressurePlateEvent("pressurePlate1", new Vector3f(-6.5f, 0.1f, 3.5f), 3f, 0.8f + HALFCHARHEIGHT, 3f, plateObj1);
        route.locEvents.add(eLoc);
        eLoc = new PressurePlateEvent("pressurePlate2", new Vector3f(-6.5f, 0.1f, -6.5f), 3f, 0.8f + HALFCHARHEIGHT, 3f, plateObj2);
        route.locEvents.add(eLoc);
        // below is for buttons
//        eInter = new DemoInteractEvent("pressurePlate1", plateObj1) {
//            @Override
//            public void onInteract(MainApplication app) {//TODO finish
//                // TODO - improve similar to levers
//                DemoRoute route = routes.get("PuzzleRoute");
//                Boolean plateDown = route.properties.getBoolean("pressurePlate1");
//                Spatial spatial = getObject().spatial;
//                if (plateDown) {
//                    spatial.setLocalTranslation(0f, 0.8f, 0f);
//                } else {
//                    spatial.setLocalTranslation(0f, 0.8f, 0f);
//                }
//            }
//        };
        route.setInteractable(pressPlate1, eInter);
        
        routes.put(route.getId(), route);

        //****** Lever Room ******//
        route = new DemoRoute("LeverRoute", "Scenes/LeverRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
                new Vector3f(1, 0, 0));
        route.properties.putInt("Lever", 0);
        // LIGHTS
        addLight(app, route, new Vector3f(0, 0, 0), new String[] {"Room"});

        // OBJECTS

        
        routes.put(route.getId(), route);
        
        //****** Button Room ******//

        route = new DemoRoute("ButtonRoute", "Scenes/ButtonRoute.j3o", new Vector3f(0, HALFCHARHEIGHT + 1.0f, 0),
               
                new Vector3f(1, 0, 0));

        // LIGHTS
        light = addLight(app, route, new Vector3f(0f, 8f, 0f), new String[] {"Room"}); // LeverBase
        
        // OBJECTS
        
        Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
        lever.setLocalTranslation(0f, 5f, 10f);
        lever.setLocalRotation(new Quaternion().fromAngles(-FastMath.PI/2, 0f,0f));
        // WARNING: Rigid body applied after this transform - axis offset
        
        // hacky but it works :)
        Spatial leverRod = ((Node) lever).descendantMatches("Lever").get(0);
        
        // object physics
        StaticDemoObject leverBaseObj = new StaticDemoObject(lever, true);
        leverBaseObj.lights.add(light);
        route.objects.add(leverBaseObj);
        
        KinematicDemoObject leverObj = new KinematicDemoObject(leverRod, 1f, false);
        leverObj.lights.add(light);
        route.objects.add(leverObj);
        
        // object events
        eInter = new InteractionEvent("lever", leverObj) {
            
            @Override
            public void onDemoEvent(MainApplication app) {
                // TODO replace with route (when moved to right position)..?
                DemoRoute leverRoute = routes.get("LeverRoute");
                int leverCount = leverRoute.properties.getInt("Lever");
                KinematicDemoObject kinematicObj = (KinematicDemoObject) getObject();
                if (leverCount < 10) {
                if (leverCount % 2 == 0) {
                    kinematicObj.queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), -FastMath.PI / 2);
                } else {
                    kinematicObj.queueRotation(app, 0.2f, new Vector3f(1f, 0f, 0), FastMath.PI / 2);
                } } else { // lol
                    app.getGameScreen().setDialogueTextSequence(new String[]{"You broke it. Well done."});
                }
                ++leverCount;
                leverRoute.properties.putInt("Lever", leverCount);
            }
            
        };
        route.setInteractable(lever, eInter);

        routes.put(route.getId(), route);

        return routes;
    }

    private static void initialiseBedroomRoute(MainApplication app, HashMap<String, DemoRoute> routes) {
        
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
}
