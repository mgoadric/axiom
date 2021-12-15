import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import java.util.*;
import java.io.*;

public class ShowBoard {

  public static SimpleUniverse universe = new SimpleUniverse();
  public static BranchGroup group = null;
  public static TransformGroup g2 = null;


  public ShowBoard(Scanner scan) {
    if (group != null) {
        group.detach();
    } else {
        group = new BranchGroup();
        group.setCapability(BranchGroup.ALLOW_DETACH);
        g2 = new TransformGroup();
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
        Color3f light1Color = new Color3f(1f, 1f, 1f); // green light
        BoundingSphere bounds =
            new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Vector3f light1Direction  = new Vector3f(4.0f, -7.0f, -12.0f);
        DirectionalLight light1
          = new DirectionalLight(light1Color, light1Direction);
        light1.setInfluencingBounds(bounds);
        group.addChild(light1);

    }
    g2.removeAllChildren();
    
    Material white = new Material( );
    white.setAmbientColor(  0.3f, 0.3f, 0.3f );
    white.setDiffuseColor(  0.6f, 0.6f, 0.6f );
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
    red.setDiffuseColor(  1.0f, 0.4f, 0f );
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
                cone = new Cylinder(0.15f, 0.9f, redAppear);
            } else {
                cone = new Cylinder(0.15f, 0.9f, whiteAppear);
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
    //universe.getViewingPlatform().setNominalViewingTransform();
    g2.addChild(new Axis());
//    Text3D t = new Text3D();
//    t.setString("X");
//    t.setPosition(new Point3f(6.0f, 0.0f, 0.51f));
//    Shape3D s = new Shape3D();
//    s.setGeometry(t);
//    g2.addChild(s);
    // add the group of objects to the Universe
    universe.addBranchGraph(group);
}

    public static void setupUniverse() {
    ViewingPlatform vp = universe.getViewingPlatform();
    TransformGroup View_TransformGroup = vp.getMultiTransformGroup().getTransformGroup(0);
    Transform3D View_Transform3D = new Transform3D();
    View_TransformGroup.getTransform(View_Transform3D);
    View_Transform3D.setTranslation(new Vector3f(0.0f,0.0f,20.0f));
	View_TransformGroup.setTransform(View_Transform3D);
}

  public static void main(String[] args) {
    try {
        Scanner scan = new Scanner(new File(args[0]));
        new ShowBoard(scan);
    } catch (FileNotFoundException fnfe) {
    }

  }
}

/*
 * Getting Started with the Java 3D API written in Java 3D
 * 
 * This program demonstrates: 1. writing a visual object class In this program,
 * Axis class defines a visual object This particular class extends Shape3D See
 * the text for a discussion. 2. Using LineArray to draw 3D lines.
 */

class Axis extends Shape3D {

    ////////////////////////////////////////////
    //
    // create axis visual object
    //
    public Axis() {

	this.setGeometry(createGeometry());

    }

    private Geometry createGeometry() {
	// create line for X axis
	IndexedLineArray axisLines = new IndexedLineArray(18,
							  GeometryArray.COORDINATES, 30);

	axisLines.setCoordinate(0, new Point3f(-5.0f, 0.0f, 0.51f));
	axisLines.setCoordinate(1, new Point3f(5.0f, 0.0f, 0.51f));
	axisLines.setCoordinate(2, new Point3f(4.9f, 0.05f, 0.56f));
	axisLines.setCoordinate(3, new Point3f(4.9f, -0.05f, 0.56f));
	axisLines.setCoordinate(4, new Point3f(4.9f, 0.05f, 0.46f));
	axisLines.setCoordinate(5, new Point3f(4.9f, -0.05f, 0.46f));
	
	axisLines.setCoordinate(6, new Point3f(0.0f, -5.0f, 0.51f));
	axisLines.setCoordinate(7, new Point3f(0.0f, 5.0f, 0.51f));
	axisLines.setCoordinate(8, new Point3f(0.05f, 4.9f, 0.56f));
	axisLines.setCoordinate(9, new Point3f(-0.05f, 4.9f, 0.56f));
	axisLines.setCoordinate(10, new Point3f(0.05f, 4.9f, 0.46f));
	axisLines.setCoordinate(11, new Point3f(-0.05f, 4.9f, 0.46f));
	
	axisLines.setCoordinate(12, new Point3f(0.0f, 0.0f, 0.51f));
	axisLines.setCoordinate(13, new Point3f(0.0f, 0.0f, 5.0f));
	axisLines.setCoordinate(14, new Point3f(0.1f, 0.1f, 4.9f));
	axisLines.setCoordinate(15, new Point3f(-0.1f, 0.1f, 4.9f));
	axisLines.setCoordinate(16, new Point3f(0.1f, -0.1f, 4.9f));
	axisLines.setCoordinate(17, new Point3f(-0.1f, -0.1f, 4.9f));

	axisLines.setCoordinateIndex(0, 0);
	axisLines.setCoordinateIndex(1, 1);
	axisLines.setCoordinateIndex(2, 2);
	axisLines.setCoordinateIndex(3, 1);
	axisLines.setCoordinateIndex(4, 3);
	axisLines.setCoordinateIndex(5, 1);
	axisLines.setCoordinateIndex(6, 4);
	axisLines.setCoordinateIndex(7, 1);
	axisLines.setCoordinateIndex(8, 5);
	axisLines.setCoordinateIndex(9, 1);
	axisLines.setCoordinateIndex(10, 6);
	axisLines.setCoordinateIndex(11, 7);
	axisLines.setCoordinateIndex(12, 8);
	axisLines.setCoordinateIndex(13, 7);
	axisLines.setCoordinateIndex(14, 9);
	axisLines.setCoordinateIndex(15, 7);
	axisLines.setCoordinateIndex(16, 10);
	axisLines.setCoordinateIndex(17, 7);
	axisLines.setCoordinateIndex(18, 11);
	axisLines.setCoordinateIndex(19, 7);
	axisLines.setCoordinateIndex(20, 12);
	axisLines.setCoordinateIndex(21, 13);
	axisLines.setCoordinateIndex(22, 14);
	axisLines.setCoordinateIndex(23, 13);
	axisLines.setCoordinateIndex(24, 15);
	axisLines.setCoordinateIndex(25, 13);
	axisLines.setCoordinateIndex(26, 16);
	axisLines.setCoordinateIndex(27, 13);
	axisLines.setCoordinateIndex(28, 17);
	axisLines.setCoordinateIndex(29, 13);

	return axisLines;

    } // end of Axis createGeometry()

} // end of class Axis
