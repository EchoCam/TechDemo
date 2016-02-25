
package uk.ac.cam.echo2016.dynademo;

import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.shadow.PointLightShadowRenderer;
import java.util.ArrayList;
import static uk.ac.cam.echo2016.dynademo.MainApplication.CHARHEIGHT;

/**
 * @author tr393
 */
public class Initialiser {
    
    /**
     * 
     * @param app - required for event subscribing and renderer attaching
     * @return 
     */
    public static ArrayList<DemoRoute> initialiseRoutes(MainApplication app) {
        ArrayList<DemoRoute> routes = new ArrayList<DemoRoute>();
        
        DemoRoute route;
        DemoLocEvent e;

        // First Route
        route = new DemoRoute("StartRoute", "Scenes/Scene1.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(1, 0, 0));
        
        Vector3f[] lightCoords = {
            new Vector3f(0f,6f,0f),
            new Vector3f(25f,6f,0f),
            new Vector3f(-25f,6f,0f),
            new Vector3f(0f,6f,-30f),
            new Vector3f(25f,6f,-30f),
            new Vector3f(-25f,6f,-30f),
            
            new Vector3f(25f,6f,-15f),
            new Vector3f(-25f,6f,-15f),
            
            new Vector3f(-60f,6f,0f),
            new Vector3f(-60f,6f,-30f)
        };
        for(Vector3f loc : lightCoords) {
            PointLight l = new PointLight();
            l.setColor(ColorRGBA.Gray);
            l.setPosition(loc);
            l.setRadius(1000f);
            route.lights.add(l);
            
            PointLightShadowRenderer plsr = new PointLightShadowRenderer(app.getAssetManager(), 1024);
            plsr.setLight(l);
            plsr.setFlushQueues(false);
            plsr.setShadowIntensity(0.01f);
            route.shadowRenderers.add(plsr);
//            viewPort.addProcessor(plsr);
        }
        
        // Starting meeting Event
        e = new DemoLocEvent(0, new Vector3f(-80, 1, -40), 40, 14, 50); 
        e.listeners.add(app);
        route.events.add(e);
        routes.add(route);

        // Second Route
        route = new DemoRoute("Parkour", "Scenes/Scene2.j3o", new Vector3f(0, (CHARHEIGHT / 2) + 2.5f, 0), new Vector3f(-1, 0, 0));
        routes.add(route);
        
        return routes;
    }
}
