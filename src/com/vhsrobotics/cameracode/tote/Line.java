package com.vhsrobotics.cameracode.tote;

import java.util.ArrayList;

import org.opencv.core.Point;
import org.opencv.core.RotatedRect;

public class Line {
	
	public Point point1;
	public Point point2;
	public Point midpoint;
	public double length;
	public RotatedRect rect1;
	public RotatedRect rect2;
	
	public Line (ArrayList<RotatedRect> targets) {
		rect1 = targets.get(0);
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).size.area() > rect1.size.area()) {
				rect1 = targets.get(i);
			}
		}
		targets.remove(rect1);
		rect2 = targets.get(0);
		for (int i = 0; i < targets.size(); i++) {
			if (targets.get(i).size.area() > rect2.size.area()) {
				rect2 = targets.get(i);
			}
		}
		point1 = new Point(rect1.center.x, rect1.center.y);
		point2 = new Point(rect2.center.x, rect2.center.y);
		
		midpoint = getMidpoint(point1, point2);
		length = getDistance(point1, point2);
	}
	
	public static double getDistance (Point a, Point b) {
		Point diff = new Point(a.x - b.x, a.y - b.y);
		return Math.sqrt(diff.x*diff.x + diff.y*diff.y);
	}
	
	public Point getMidpoint (Point a, Point b) {
		return new Point((a.x + b.x) / 2, (a.y + b.y) / 2);
	}
	
}
