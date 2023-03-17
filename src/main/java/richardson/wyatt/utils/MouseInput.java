package richardson.wyatt.utils;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class MouseInput extends GLFWCursorPosCallback{
	
	public static float deltaX = 0;
	public static float deltaY = 0;
	
	private static float lastDX = 0;
	private static float lastDY = 0;
	private static float lastXPos = 0;
	private static float lastYPos = 0;
	@Override
	public void invoke(long window, double xpos, double ypos) {
		deltaX = (float) xpos - lastXPos;
		deltaY = (float) ypos - lastYPos;
		lastXPos = (float) xpos;
		lastYPos = (float) ypos;
		
	}
	
	public static void checkForStillMouse() {
		if(lastDX == deltaX && lastDY == deltaY) {
			deltaX = 0;
			deltaY = 0;
		}
		lastDX = deltaX;
		lastDY = deltaY;
	}
	
}
