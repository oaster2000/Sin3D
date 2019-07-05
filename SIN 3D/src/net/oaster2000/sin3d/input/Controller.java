package net.oaster2000.sin3d.input;

public class Controller {

	public double x, z, rotation, xa, za, rotationa;
	public static boolean turnLeft = false;
	public static boolean turnRight = false;
	
	public void tick(boolean forward, boolean back, boolean left, boolean right) {
		double rotationSpeed = 0.05;
		double walkSpeed = 1;
		double xMove = 0;
		double zMove = 0;
		
		if(forward) {
			zMove++;
		}
		if(back) {
			zMove--;
		}
		
		if(right) {
			xMove++;
		}
		if(left) {
			xMove--;
		}
		
		if(turnRight) {
			rotationa += rotationSpeed;
		}
		if(turnLeft) {
			rotationa -= rotationSpeed;
		}
		
		xa += (xMove * Math.cos(rotation) + zMove * Math.sin(rotation)) * walkSpeed;
		za += (zMove * Math.cos(rotation) - xMove * Math.sin(rotation)) * walkSpeed;
		
		x += xa;
		z += za;
		xa *= 0.1;
		za *= 0.1;
		rotation += rotationa;
		rotationa *= 0.1;
	}
	
}
