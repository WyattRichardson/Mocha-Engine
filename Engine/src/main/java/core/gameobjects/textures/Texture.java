package core.gameobjects.textures;
import static org.lwjgl.opengl.GL11.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
public abstract class Texture {
	
	public String fName;
	public int ID;
	private int width, height;
	private float[] pixels;
	
	public Texture(String fName) {
		
	}

}
