package richardson.wyatt.mocha.utils;


import richardson.wyatt.mocha.application.Window;

import static org.lwjgl.glfw.GLFW.*;


public class MouseInput{
	

	public static int deltaX = 0;
	public static int deltaY = 0;
	private static int lastXPos = 0;
	private static int lastYPos = 0; 
	

	public static void tick() {
		double[] xPosArr = new double[1];
		double[] yPosArr = new double[1];
		glfwGetCursorPos(Window.id, xPosArr, yPosArr);
		int xPos = (int)xPosArr[0];
		int yPos = (int)yPosArr[0];
		deltaX = xPos - lastXPos;
		deltaY = yPos - lastYPos;
		lastXPos = xPos;
		lastYPos = yPos;
		
	}

	

}
