package richardson.wyatt.mocha.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.joml.Vector2f;
import org.joml.Vector3f;

import richardson.wyatt.mocha.utils.xml.XmlNode;
import richardson.wyatt.mocha.utils.xml.XmlParser;


public class ColladaLoader {

	public static float[] vertices;
	public static float[] texCoords;
	public static float[] normals;
	public static int[] indices;

	private static FileReader fr;
	private static BufferedReader reader;
	private static String path;

	private static ArrayList<Vector3f> vertsArray;
	private static ArrayList<Vector2f> tCArray;
	private static ArrayList<Vector3f> normsArray;
	private static ArrayList<String[]> faces;

	public static void readCollada(String path) throws FileNotFoundException { 
		ColladaLoader.path = path;
		fr = new FileReader(new File(path));
		reader = new BufferedReader(fr);
		
		readToArrays();
		arrangeData();
		flushArrays();
      
	}

	private static void readToArrays() {
		
		vertsArray = new ArrayList<Vector3f>();
		tCArray = new ArrayList<Vector2f>();
		normsArray = new ArrayList<Vector3f>();
		faces = new ArrayList<String[]>();

		XmlNode collada = XmlParser.loadXmlFile("src/main/resources/assets/models/LowPolyCharacter.dae");
		readGeometries(collada);

	}

	private static void readGeometries(XmlNode collada) {
		List<XmlNode> sourceNodes = collada.getChild("library_geometries").getChild("geometry").getChild("mesh").getChildren("source"); 
		String positionsString = sourceNodes.get(0).getChild("float_array").getData();
		String[] vertPositions = positionsString.split(" ");
		Vector3f pos = new Vector3f();
		for (int i = 0; i < vertPositions.length; i++) { // Adds all the vertex positions found in vertPositions array
															// into vertsArray as Vector3f objects.
			if ((i + 1) % 3 == 1) {
				pos.x = Float.parseFloat(vertPositions[i]);
				pos.y = Float.parseFloat(vertPositions[i + 1]);
				pos.z = Float.parseFloat(vertPositions[i + 2]);
				vertsArray.add(pos);
				pos = new Vector3f();
			}
		}
		String normalsString = sourceNodes.get(1).getChild("float_array").getData();
		String[] norms = normalsString.split(" ");
		Vector3f norm = new Vector3f();
		for (int i = 0; i < norms.length; i++) { // See above.
			if ((i + 1) % 3 == 1) {
				norm.x = Float.parseFloat(norms[i]);
				norm.y = Float.parseFloat(norms[i + 1]);
				norm.z = Float.parseFloat(norms[i + 2]);
				normsArray.add(norm);
				norm = new Vector3f();
			}
		}
		String texCoordsString = sourceNodes.get(2).getChild("float_array").getData();
		String[] tCs = texCoordsString.split(" ");
		Vector2f tC = new Vector2f();
		for (int i = 0; i < tCs.length; i++) {
			if ((i + 1) % 2 == 1) {
				tC.x = Float.parseFloat(tCs[i]);
				tC.y = Float.parseFloat(tCs[i + 1]);
				tCArray.add(tC);
				tC = new Vector2f();
			}
		}
		String facesString = collada.getChild("library_geometries").getChild("geometry").getChild("mesh").getChild("triangles").getChild("p").getData();
		String[] elements = facesString.split(" ");
		String[] face = new String[3];
		for (int i = 0; i < elements.length; i += 9) {
			StringBuilder builder = new StringBuilder();
			face[0] = elements[i] + "/" + elements[i + 1] + "/" + elements[i + 2];
			face[1] = elements[i + 3] + "/" + elements[i + 4] + "/" + elements[i + 5];
			face[2] = elements[i + 6] + "/" + elements[i + 7] + "/" + elements[i + 8];
			faces.add(face);
			face = new String[3];
		}
		
		
	}

	private static String findTagName(String line) {
		Pattern p = Pattern.compile("<(.+?)>");
		Matcher m = p.matcher(line);
		m.find();
		return m.group(1);

	}

	private static void arrangeData() { // fill raw arrays according to face data

		vertices = new float[vertsArray.size() * 3];


		for (int i = 0; i < vertsArray.size(); i++) { // fill raw vertices array
			
			vertices[(i * 3)] = vertsArray.get(i).x;
			vertices[(i * 3) + 1] = vertsArray.get(i).y;
			vertices[(i * 3) + 2] = vertsArray.get(i).z;
			
		}
		

		texCoords = new float[vertsArray.size() * 2];
		normals = new float[vertsArray.size() * 3];
		indices = new int[faces.size() * 3]; // allocate raw indices array 

		for (int i = 0; i < faces.size(); i++) { // go through every vertex in every face and sort/fill accordingly

			String[] face = faces.get(i);

			for (int z = 0; z < 3; z++) {
				String[] currentVertex = face[z].split("/");
				int currentIndex = Integer.parseInt(currentVertex[0]);
				
				indices[(i * 3) + z] = currentIndex; // passing currentIndex of every vertex in every face into raw
				Vector3f currentNormal = normsArray.get(Integer.parseInt(currentVertex[1]));
			
				Vector2f currentTexCoord = tCArray.get(Integer.parseInt(currentVertex[2]));

				normals[(currentIndex * 3)] = currentNormal.x;
				normals[(currentIndex * 3) + 1] = currentNormal.y;
				normals[(currentIndex * 3) + 2] = currentNormal.z;

				texCoords[(currentIndex * 2)] = currentTexCoord.x;
				texCoords[(currentIndex * 2) + 1] = currentTexCoord.y;

			}
			
			

		}
	}

	private static void flushArrays() { // flush arrayLists

		vertsArray.clear();
		vertsArray = null;
		normsArray.clear();
		normsArray = null;
		tCArray.clear();
		tCArray = null;

	}

	public static void flushData() { // flush raw arrays
		vertices = null;
		indices = null;
		texCoords = null;
		normals = null;
	}
}
