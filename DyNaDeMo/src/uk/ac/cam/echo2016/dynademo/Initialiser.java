package uk.ac.cam.echo2016.dynademo;

import com.jme3.bullet.control.RigidBodyControl;
import static uk.ac.cam.echo2016.dynademo.MainApplication.CHARHEIGHT;

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
        HashMap<String, DemoRoute> routes = new HashMap<String, DemoRoute>();

        DemoRoute route;
        DemoLocEvent eLoc;
        DemoInteractEvent eInter;
        DemoLight light;

        // ////// Bedroom ////////
        route = new DemoRoute("Bedroom", "Scenes/Bedroom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0),
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
        eLoc = new DemoLocEvent("Node1", new Vector3f(-80, 1, -40), 40, 14, 50);
        eLoc.addListener(app);
        route.events.add(eLoc);
        routes.put(route.getId(), route);

        // ////// Puzzle Room ////////
        route = new DemoRoute("PuzzleRoom", "Scenes/PuzzleRoom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0),
                new Vector3f(0, 0, -1));

        // LIGHTS
        light = addLight(app, route, new Vector3f(0, 8f, 0), new String[] {"Room"}); // Cube

        // OBJECTS
        Spatial crate = app.getAssetManager().loadModel("Models/Crate.j3o");
        crate.setLocalTranslation(0, 0, -30);
        // object physics
        DemoDynamic crateObj = new DemoDynamic(crate, 5f, true);
        crateObj.lights.add(light);
        route.objects.add(crateObj);
        // object events
        eInter = new DemoInteractEvent("crate", crateObj){
            @Override
            public void onInteract(MainApplication app) {
                app.drag(getObject().spatial);
            }
        };
        eInter.addListener(app);
        route.setInteractable(crate, eInter);

        routes.put(route.getId(), route);

        // ////// Lever Room ////////
        route = new DemoRoute("LeverRoom", "Scenes/LeverRoom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        addLight(app, route, new Vector3f(0, 0, 0), new String[] {"Room"});

        // OBJECTS
        // Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
        // Material leverMat = new Material(app.getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        // lever.setMaterial(leverMat);
        // lever.setLocalTranslation(0f, 0f, 0f);

        // ////// Button Room ////////

        route = new DemoRoute("ButtonRoom", "Scenes/ButtonRoom.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0),
                new Vector3f(1, 0, 0));

        // LIGHTS
        light = addLight(app, route, new Vector3f(0f, 8f, 0f), new String[] {"Room"}); // LeverBase
        
        // OBJECTS
        
        Spatial lever = app.getAssetManager().loadModel("Models/Lever.j3o");
        lever.setLocalTranslation(0f, 5f, 10f);
        lever.setLocalRotation(new Quaternion().fromAngles(-FastMath.PI/2, 0f,0f));
        // WARNING: Rigid body applied after this transform - local transform offset
        
        // hacky but it works :)
        Spatial leverRod = ((Node) lever).descendantMatches("Lever").get(0);
        
        // object physics
        DemoStatic leverBaseObj = new DemoStatic(lever, true);
        leverBaseObj.lights.add(light);
        route.objects.add(leverBaseObj);
        
        DemoKinematic leverObj = new DemoKinematic(leverRod, 1f, false);
        leverObj.lights.add(light);
        route.objects.add(leverObj);
        
        // object events
        eInter = new DemoInteractEvent("lever", leverObj) {
            
            @Override
            public void onInteract(MainApplication app) {
                Spatial spatial = getObject().spatial;
                RigidBodyControl rbc = spatial.getControl(RigidBodyControl.class);
                // TODO should check parent nodes for physics controls?
                if (rbc == null) {
                    throw new NullPointerException("No valid physics control found for object: " + spatial.getName());
                }
                if (!(getObject() instanceof DemoKinematic)) {
                    throw new RuntimeException("Translation event called but object: "
                            + spatial.getName() + " is not kinematic");
                }
                DemoKinematic kinematicObj = (DemoKinematic) getObject();
                kinematicObj.queueRotation(app, 1f, new Vector3f(1f, 0f, 0), -FastMath.PI / 2);
            }
            
        };
        eInter.addListener(app);
        route.setInteractable(lever, eInter);

        routes.put(route.getId(), route);

        return routes;
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
