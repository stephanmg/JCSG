/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.mihosoft.vrl.v3d.jcsg.samples;

import eu.mihosoft.vrl.v3d.jcsg.CSG;
import eu.mihosoft.vrl.v3d.jcsg.Cylinder;
import eu.mihosoft.vrl.v3d.jcsg.FileUtil;
import eu.mihosoft.vrl.v3d.jcsg.Transform;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Michael Hoffer &lt;info@michaelhoffer.de&gt;
 */
public class Naze32Holder {

    public CSG toCSG() {

        double w = 36;
        double h = 36;
        double wInner = 31;
        double hInner = 31;

        double r = 1.5;
        double screwR = 1.25;
        double screwHolderHeight = 4;

        double thickness = 2.0;

        int resolution = 16;

        CSG base = basePlatform(r, thickness, resolution, w, h);

//        CSG pegPrototype = new Peg().setBoardMountingHeight(2).setOverlap(0.6).
//                setPegDepth(4).setOuterOffset(2).toCSG();
//
//        CSG peg1 = pegPrototype.transformed(Transform.unity().rotX(180).translate(-w / 2.0, -h / 4.0, 0));
//        CSG peg2 = pegPrototype.transformed(Transform.unity().rotX(180).mirror(Plane.YZ_PLANE).translate(-w / 2.0, 0, 0));
        
        CSG screCylPrototype = new Cylinder(screwR,screwHolderHeight, resolution).toCSG().transformed(Transform.unity().translateZ(thickness));
        CSG cyl1 = screCylPrototype.transformed(Transform.unity().translateX(-wInner / 2.0).translateY(-hInner / 2.0));
        CSG cyl2 = screCylPrototype.transformed(Transform.unity().translateX(wInner / 2.0).translateY(-hInner / 2.0));
        CSG cyl3 = screCylPrototype.transformed(Transform.unity().translateX(wInner / 2.0).translateY(hInner / 2.0));
        CSG cyl4 = screCylPrototype.transformed(Transform.unity().translateX(-wInner / 2.0).translateY(hInner / 2.0));

        return base.union(/*peg1, peg2, */cyl1,cyl2,cyl3,cyl4);
    }

    private CSG basePlatform(double r, double thickness, int resolution, double w, double h) {
        CSG cylPrototype = new Cylinder(r, thickness, resolution).toCSG();
        CSG cyl1 = cylPrototype.transformed(Transform.unity().translateX(-w / 2.0 + r).translateY(-h / 2.0+r));
        CSG cyl2 = cylPrototype.transformed(Transform.unity().translateX(w / 2.0 -r).translateY(-h / 2.0+r));
        CSG cyl3 = cylPrototype.transformed(Transform.unity().translateX(w / 2.0-r).translateY(h / 2.0-r));
        CSG cyl4 = cylPrototype.transformed(Transform.unity().translateX(-w / 2.0+r).translateY(h / 2.0-r));
        CSG base = cyl1.hull(cyl2, cyl3, cyl4);
        return base;
    }

    public static void main(String[] args) throws IOException {

        FileUtil.write(new File("naze32-mount.stl"), new Naze32Holder().toCSG().toStlString());

        new Naze32Holder().toCSG().toObj().toFiles(new File("naze32-mount.obj"));

    }

}