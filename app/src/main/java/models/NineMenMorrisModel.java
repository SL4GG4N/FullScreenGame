package models;

/**
 * Created by simonlundstrom on 25/11/16.
 */
public class NineMenMorrisModel {
    private Point[] points;

    public Point getPoint(int i) {
        return points[i];
    }

    public Point[] getPoints() {
        return points;
    }

    public int indexOf(Point p) {
        for (int i=0; i<points.length; i++)
            if (points[i]==p)
                return i;
        return -1;
    }

    public NineMenMorrisModel() {
        points = new Point[24];
        points[0]=new Point(3,3);
        points[1]=new Point(2,2);
        points[2]=new Point(1,1);
        points[3]=new Point(4,3);
        points[4]=new Point(4,2);
        points[5]=new Point(4,1);
        points[6]=new Point(5,3);
        points[7]=new Point(6,2);
        points[8]=new Point(7,1);
        points[9]=new Point(5,4);
        points[10]=new Point(6,4);
        points[11]=new Point(7,4);
        points[12]=new Point(5,5);
        points[13]=new Point(6,6);
        points[14]=new Point(7,7);
        points[15]=new Point(4,5);
        points[16]=new Point(4,6);
        points[17]=new Point(4,7);
        points[18]=new Point(3,5);
        points[19]=new Point(2,6);
        points[20]=new Point(1,7);
        points[21]=new Point(3,4);
        points[22]=new Point(2,4);
        points[23]=new Point(1,4);
    }
}
