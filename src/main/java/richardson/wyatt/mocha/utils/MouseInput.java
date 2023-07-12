package richardson.wyatt.mocha.utils;

import org.lwjgl.glfw.GLFWCursorPosCallback;

import richardson.wyatt.mocha.application.Window;

import static org.lwjgl.glfw.GLFW.*;

import java.nio.DoubleBuffer;

public class MouseInput extends GLFWCursorPosCallback{
	
	public static Window window;
	public static int deltaX = 0;
	public static int deltaY = 0;
	private static boolean frameInvoked = false;
	private static int lastDX = 0;
	private static int lastDY = 0;
	private static int lastXPos = 0;
	private static int lastYPos = 0; 
	
	public MouseInput() {
//		DoubleBuffer xBuffer = DoubleBuffer.allocate(1);
//		glfwGetCursorPos(Window.id, xBuffer, null);
//		int xPos = (int) xBuffer.get();
//		System.out.println(xPos);
	}

	@Override
	public void invoke(long window, double xpos, double ypos) {
		deltaX = (int)xpos - lastXPos;
		deltaY = (int)ypos - lastYPos;
		//deltaX += (deltaX - lastDX);
		//deltaY += (deltaY - lastDY);
		
		lastXPos = (int)xpos;
		lastYPos = (int)ypos;
		lastDX = deltaX;
		lastDY = deltaY;
		frameInvoked = true;
		
	}
	
	public static void checkForStillMouse() {
		if(frameInvoked == false) {
			deltaX = 0;
			deltaY = 0;

		}
		
	}
	
	public static void setFrameInvoked(boolean b) {
		frameInvoked = b;
	}
}
