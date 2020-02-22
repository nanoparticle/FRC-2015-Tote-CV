package com.vhsrobotics.cameracode.tote;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.CvType;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

//import edu.wpi.first.wpilibj.networktables.NetworkTable;


public class Tote {
	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//NetworkTable.setClientMode();
		//NetworkTable.setIPAddress("roboRIO-2085.local");
	}
	
	//static NetworkTable table = NetworkTable.getTable("cameracode");

	//static VideoCapture cap = new VideoCapture();
	static VideoCapture cap = new VideoCapture(0);
	static Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(3, 3));
	static Mat mat = new Mat(640, 480, CvType.CV_8UC3);
	static Mat hsv = new Mat(640, 480, CvType.CV_8UC3);
	static Mat img1 = new Mat(640, 480, CvType.CV_8UC3);
	static ArrayList<RotatedRect> targets = new ArrayList<RotatedRect>();
	static ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();
	static Imshow window = new Imshow("camera");
	static Line line;

	public static void main(String[] args) {
		//cap.open("http://10.20.85.11/mjpg/video.mjpg");
		if (cap.isOpened()) {
			System.out.println("Connected to Camera");
			//flushCamera();
			while (true) {
				targets = new ArrayList<RotatedRect>();
				contours = new ArrayList<MatOfPoint>();
				cap.read(mat);
				
				//Imgproc.cvtColor(mat, hsv, Imgproc.COLOR_BGR2HSV);
				hsv = mat.clone();
				
				//Core.inRange(hsv, new Scalar(80, 150, 150), new Scalar(95, 255, 255), img1);
				Core.inRange(hsv, new Scalar(205, 205, 205), new Scalar(255, 255, 255), img1);
				
				Imgproc.morphologyEx(img1, img1, Imgproc.MORPH_OPEN, kernel);
				
				Imgproc.findContours(img1.clone(), contours, new Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);
				
				for (int i=0; i < contours.size(); i++) {
					Point[] points = new Point[4];
					MatOfPoint2f temp = new MatOfPoint2f();
					contours.get(i).convertTo(temp, CvType.CV_32FC2);
					RotatedRect rect = Imgproc.minAreaRect(temp);
					rect.points(points);
					if (rect.size.area() > 15000) {
						Imgproc.line(mat, points[0], points[1], new Scalar(0, 0, 255), 3);
						Imgproc.line(mat, points[1], points[2], new Scalar(0, 0, 255), 3);
						Imgproc.line(mat, points[2], points[3], new Scalar(0, 0, 255), 3);
						Imgproc.line(mat, points[3], points[0], new Scalar(0, 0, 255), 3);
						Imgproc.circle(mat, rect.center, 3, new Scalar(255, 0, 0), 3);
						targets.add(rect);
					}
				}
				if (targets.size() >= 2) {
					line = new Line(targets);
					
					Imgproc.line(mat, line.point1, line.point2, new Scalar(255, 0, 0), 3);
					
					if (line.midpoint.x < (mat.width() / 2) + 100 && line.midpoint.x > (mat.width() / 2) - 100) {
						Imgproc.circle(mat, line.midpoint, 3, new Scalar(0, 255, 0), 3);
						//table.putBoolean("tote", true);
					} else {
						Imgproc.circle(mat, line.midpoint, 3, new Scalar(255, 0, 0), 3);
						//table.putBoolean("tote", false);
					}
					
					System.out.println(line.length);
					
				} else {
					//table.putBoolean("tote", false);
				}
				
				//table.putNumber("rect", targets.size());
				
				window.showImage(mat);
			}
		} else {
			System.out.println("Connection Failed.");
		}
		
	}
	
	public static void flushCamera () {
		for (int i = 0; i < 150; i++) {
			cap.grab();
		}
	}
}