package core.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL30.*;

public class OBJLoader {

	public static float[] vertices;
	public static float[] texCoords;
	public static float[] normals;
	public static int[] indices;
	
	private static int faceType;
	private static FileReader fr;
	private static BufferedReader reader;
	private static String path;
	
	private static ArrayList<Vector3f> vertsArray;
	private static ArrayList<Vector2f> tcArray;
	private static ArrayList<Vector3f> normsArray;
	private static ArrayList<String[]> faces;
	
	public static void readOBJ(String path, int faceType) throws FileNotFoundException { //TODO: Check to see if data contains normals and texcoords and exclude arranging them if not.
		OBJLoader.faceType = faceType;
		OBJLoader.path = path;
		fr = new FileReader(new File(path));
		reader = new BufferedReader(fr);
		
		readToArrays();
		arrangeData();
		flushArrays();
		
	}
	
	private static void readToArrays() {
		
		vertsArray = new ArrayList<Vector3f>();
		tcArray = new ArrayList<Vector2f>();
		normsArray = new ArrayList<Vector3f>();
		faces = new ArrayList<String[]>();
		
		String line;
		try {
			line = reader.readLine();
			while(line != null) {
				
				String[] elements = line.split(" ");
				if(line.startsWith("v ")) {
				
					vertsArray.add(new Vector3f(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]), Float.parseFloat(elements[3])));
				
				}else if(line.startsWith("vt ")) {
					
					tcArray.add(new Vector2f(Float.parseFloat(elements[1]), Float.parseFloat(elements[2])));
				
				}else if(line.startsWith("vn ")) {
				
					normsArray.add(new Vector3f(Float.parseFloat(elements[1]), Float.parseFloat(elements[2]), Float.parseFloat(elements[3])));

					
				}else if(line.startsWith("f ")) {
					
					if(faceType == GL_QUADS) {
						faces.add(new String[]{elements[1], elements[2], elements[3], elements[4]});
					}else if(faceType == GL_TRIANGLES) {
						faces.add(new String[]{elements[1], elements[2], elements[3]});
					}
					
				}
				
				line = reader.readLine();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Could not read file: " + path);
		}
		
	}
	
	private static void arrangeData() { //fill raw arrays according to face data
		
		vertices = new float[vertsArray.size() * 3];
		for(int i = 0; i < vertsArray.size(); i++) { //fill raw vertices array
			
			vertices[(i*3)] = vertsArray.get(i).x;
			vertices[(i*3) + 1] = vertsArray.get(i).y;
			vertices[(i*3) + 2] = vertsArray.get(i).z;
			
		}
		
		texCoords = new float[vertsArray.size() * 2];
		normals = new float[vertsArray.size() * 3];
		
		if(faceType == GL_TRIANGLES) { //allocate raw indices array according to faceType
			indices = new int[faces.size() * 3];
		}else if(faceType == GL_QUADS) {
			indices = new int[faces.size() * 4];
		}
		
		for(int i = 0; i < faces.size(); i++) { //go through every vertex in every face and sort/fill accordingly
			
			String[] face = faces.get(i);
			
			if(faceType == GL_TRIANGLES) {
				
				for(int z = 0; z < 3; z++) {
					
					String[] currentVertex = face[z].split("/");
					int currentIndex = Integer.parseInt(currentVertex[0]) - 1;
					indices[(i*3) + z] = currentIndex; //passing currentIndex of every vertex in every face into raw indices array
					Vector3f currentNormal = normsArray.get(Integer.parseInt(currentVertex[2]) - 1);
					Vector2f currentTexCoord = tcArray.get(Integer.parseInt(currentVertex[1]) - 1);
					
					normals[(currentIndex*3)] = currentNormal.x;
					normals[(currentIndex*3) + 1] = currentNormal.y;
					normals[(currentIndex*3) + 2] = currentNormal.z;
					
					texCoords[(currentIndex*2)] = currentTexCoord.x;
					texCoords[(currentIndex*2) + 1] = currentTexCoord.y;
							
				}
				
				
			}else if(faceType == GL_QUADS) {
				
				for(int z = 0; z < 4; z++) {
					
					String[] currentVertex = face[z].split("/");
					int currentIndex = Integer.parseInt(currentVertex[0]) - 1;
					indices[(i*4) + z] = currentIndex;//passing currentIndex of every vertex in every face into raw indices array
					Vector3f currentNormal = normsArray.get(Integer.parseInt(currentVertex[2]) - 1);
					Vector2f currentTexCoord = tcArray.get(Integer.parseInt(currentVertex[1]) - 1);
					
					normals[(currentIndex*3)] = currentNormal.x;
					normals[(currentIndex*3) + 1] = currentNormal.y;
					normals[(currentIndex*3) + 2] = currentNormal.z;
					
					texCoords[(currentIndex*2)] = currentTexCoord.x;
					texCoords[(currentIndex*2) + 1 ] = currentTexCoord.y;
							
				}
				
			}
			
		}
		
		flushArrays();//no longer need arrayLists
		
	}
	
	private static void flushArrays() { //flush arrayLists
		
		vertsArray = null;
		normsArray = null;
		tcArray = null;
		
	}
	
	public static void flushData() { //flush raw arrays
		
		vertices = null;
		indices = null;
		texCoords = null;
		normals = null;
		
	}
	
}
