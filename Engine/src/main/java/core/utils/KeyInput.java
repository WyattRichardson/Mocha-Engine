package core.utils;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallbackI;


public class KeyInput implements GLFWKeyCallbackI{

	public static HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();

	public static boolean isKeyDown(int key) {
		if(keys.containsKey(key)) {
			return keys.get(key);
		}else{
			return false;
		}
	}

	public void invoke(long window, int key, int scancode, int action, int mods) {
		keys.put(key, action != GLFW_RELEASE);
		
	}
	

}
