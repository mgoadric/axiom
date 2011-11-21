import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import java.util.*;
import java.io.*;

public class ShowBoard {

  public ShowBoard(String filename) {
    SimpleUniverse universe = new SimpleUniverse();
    BranchGroup group = new BranchGroup();
    TransformGroup g2 = new TransformGroup();
    g2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    g2.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    MouseRotate myMouseRotate = new MouseRotate();
    myMouseRotate.setTransformGroup(g2);
    myMouseRotate.setSchedulingBounds(new BoundingSphere());
    group.addChild(myMouseRotate);

    MouseTranslate myMouseTranslate = new MouseTranslate();
    myMouseTranslate.setTransformGroup(g2);
    myMouseTranslate.setSchedulingBounds(new BoundingSphere());
    group.addChild(myMouseTranslate);

    MouseZoom myMouseZoom = new MouseZoom();
    myMouseZoom.setTransformGroup(g2);
    myMouseZoom.setSchedulingBounds(new BoundingSphere());
    group.addChild(myMouseZoom);
    group.addChild(g2);
    
    Material white = new Material( );
    white.setAmbientColor(  0.3f, 0.3f, 0.3f );
    white.setDiffuseColor(  1.0f, 1.0f, 1.0f );
    white.setEmissiveColor( 0.0f, 0.0f, 0.0f );
    white.setSpecularColor( 1.0f, 1.0f, 1.0f );
    white.setShininess( 64.0f );
    Appearance whiteAppear = new Appearance( );
    whiteAppear.setMaterial( white );

    Material blue = new Material( );
    blue.setAmbientColor(  0.3f, 0.3f, 0.3f );
    blue.setDiffuseColor(  0f, 0f, 1.0f );
    blue.setEmissiveColor( 0.0f, 0.0f, 0.0f );
    blue.setSpecularColor( 1.0f, 1.0f, 1.0f );
    blue.setShininess( 64.0f );
    Appearance blueAppear = new Appearance( );
    blueAppear.setMaterial( blue );

    Material red = new Material( );
    red.setAmbientColor(  0.3f, 0.3f, 0.3f );
    red.setDiffuseColor(  1.0f, 0f, 0f );
    red.setEmissiveColor( 0.0f, 0.0f, 0.0f );
    red.setSpecularColor( 1.0f, 1.0f, 1.0f );
    red.setShininess( 64.0f );
    Appearance redAppear = new Appearance( );
    redAppear.setMaterial( red );

    for (int i = -4; i < 5; i++) {
        for (int j = -4; j < 5; j++) {

            TransformGroup tgtable = new TransformGroup();
            Transform3D transformtable = new Transform3D();
            Box b = null;
            b = new Box(.49f, .49f, 0.01f, blueAppear);
            Vector3f vectortable = new Vector3f(i, j, 0.5f);
            transformtable.setTranslation(vectortable);
            tgtable.setTransform(transformtable);
            tgtable.addChild(b);
            g2.addChild(tgtable); 
        }       
    }

    try {
    Scanner scan = new Scanner(new File(filename));
    while (scan.hasNext()) {    
        String line = scan.nextLine();
        String color = line.substring(2, 7);
        String type = line.substring(9, 10);
        int so = line.indexOf("(", line.indexOf("(") + 1);
        int se = line.indexOf(")", so + 1);
        int fc = line.indexOf(",", so);
        int sc = line.indexOf(",", fc + 1);
        float x = Integer.parseInt(line.substring(so + 1, fc));
        float y = Integer.parseInt(line.substring(fc + 1, sc));
        float z = Integer.parseInt(line.substring(sc + 1, se));
        
        if (type.equals("C")) {
            TransformGroup tg = new TransformGroup();
            Transform3D transform = new Transform3D();
            Box cone = null;
            if (color.equals("black")) {
                cone = new Box(0.49f, 0.49f, 0.49f, redAppear);
            } else {
                cone = new Box(0.49f, 0.49f, 0.49f, whiteAppear);
            }
            Vector3f vector = new Vector3f(x, y, z);
            transform.setTranslation(vector);
            tg.setTransform(transform);
            tg.addChild(cone);
            g2.addChild(tg); 
            
            // add domes
            String dir = line.substring(se + 1, se + 2);
            float which = .5f;
            if (line.substring(se + 2, se + 3).equals("-")) {
                which = -.5f;
            }
            if (dir.equals("x")) {
                x += which;
            } else if (dir.equals("y")) {
                y += which;
            } else if (dir.equals("z")) {
                z += which;
            }
            
             tg = new TransformGroup();
             transform = new Transform3D();
            Sphere sp = null;
            if (color.equals("black")) {
                sp = new Sphere(0.25f, redAppear);
            } else {
                sp = new Sphere(0.25f, whiteAppear);
            }
             vector = new Vector3f(x, y, z);
            transform.setTranslation(vector);
            tg.setTransform(transform);
            tg.addChild(sp);
            g2.addChild(tg); 
            
            if (se + 3 != line.length()) {
                x = Integer.parseInt(line.substring(so + 1, fc));
                y = Integer.parseInt(line.substring(fc + 1, sc));
                z = Integer.parseInt(line.substring(sc + 1, se));
                 dir = line.substring(se + 4, se + 5);
                 which = .5f;
                if (line.substring(se + 5, se + 6).equals("-")) {
                    which = -.5f;
                }
                if (dir.equals("x")) {
                    x += which;
                } else if (dir.equals("y")) {
                    y += which;
                } else if (dir.equals("z")) {
                    z += which;
                }
                
                 tg = new TransformGroup();
                 transform = new Transform3D();
                 sp = null;
                if (color.equals("black")) {
                    sp = new Sphere(0.25f, redAppear);
                } else {
                    sp = new Sphere(0.25f, whiteAppear);
                }
                 vector = new Vector3f(x, y, z);
                transform.setTranslation(vector);
                tg.setTransform(transform);
                tg.addChild(sp);
                g2.addChild(tg); 
            

            }
            
        
        } else { //sceptre adding
            Transform3D rot = new Transform3D();
            String dir = line.substring(se + 1, line.length() - 1);
            int which = 1;
            if (line.substring(line.length() - 1).equals("-")) {
                which = -1;
            }
            if (dir.equals("x")) {
                x += which;
                rot.rotZ(Math.PI/2);
            } else if (dir.equals("y")) {
                y += which;
            } else if (dir.equals("z")) {
                z += which;
                rot.rotX(Math.PI/2);
            }
            TransformGroup tg = new TransformGroup();
            TransformGroup rotTG = new TransformGroup();
            Transform3D transform = new Transform3D();
            Cylinder cone = null;
            if (color.equals("black")) {
                cone = new Cylinder(0.15f, 1f, redAppear);
            } else {
                cone = new Cylinder(0.15f, 1f, whiteAppear);
            }
            Vector3f vector = new Vector3f(x, y, z);
            transform.setTranslation(vector);
            
            rotTG.setTransform(rot);
            
            tg.setTransform(transform);
            rotTG.addChild(cone);
            tg.addChild(rotTG);
            g2.addChild(tg);
        }
    }
    } catch (FileNotFoundException fnfe) {
    }
    Color3f light1Color = new Color3f(1f, 1f, 1f); // green light
    BoundingSphere bounds =
	    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
    Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);
    DirectionalLight light1
      = new DirectionalLight(light1Color, light1Direction);
    light1.setInfluencingBounds(bounds);
    group.addChild(light1);
    //universe.getViewingPlatform().setNominalViewingTransform();

    ViewingPlatform vp = universe.getViewingPlatform();
    TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0);
    Transform3D View_Transform3D = new Transform3D();
    View_TransformGroup.getTransform(View_Transform3D);
    View_Transform3D.setTranslation(new Vector3f(0.0f,0.0f,20.0f));
	View_TransformGroup.setTransform(View_Transform3D); 


    // add the group of objects to the Universe
    universe.addBranchGraph(group);
}
  public static void main(String[] args) {
    new ShowBoard(args[0]);
  }
}
