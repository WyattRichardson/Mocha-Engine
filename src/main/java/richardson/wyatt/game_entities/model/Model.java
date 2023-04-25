package richardson.wyatt.game_entities.model;
import static org.lwjgl.opengl.GL30.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector3f;

import richardson.wyatt.game_entities.entity.EntityComponent;
import richardson.wyatt.game_entities.textures.ModelTexture;
import richardson.wyatt.utils.ColladaLoader;
import richardson.wyatt.utils.OBJLoader;
public class Model extends EntityComponent{
	
	private int vaoID;
	
	private int faceType;
	
	private int indicyCount;
	
	private ModelTexture texture;

	public Model(String fName, int faceType){
		super(Type.MODEL);
		this.faceType = faceType;
		if(fName.endsWith(".obj")) {
			loadFromOBJ(fName);
		}else if(fName.endsWith(".dae")) {
			loadFromCollada(fName);
		}
	}
	
	public Model(int faceType) {
		super(Type.MODEL);
		this.faceType = faceType;
	}

	private void loadFromCollada(String fName) {
		vaoID = glGenVertexArrays();
		try {
			ColladaLoader.readCollada("src/main/resources/assets/models/" + fName);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.print("Could not find Collada File: " + fName);
		}
		glBindVertexArray(vaoID);
		int vertsVBOID = glGenBuffers();
		int normsVBOID = glGenBuffers();
		int tcVBOID = glGenBuffers();
		int indVBOID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indVBOID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, ColladaLoader.indices, GL_STATIC_DRAW);
		this.indicyCount = ColladaLoader.indices.length;
		
		glBindBuffer(GL_ARRAY_BUFFER, vertsVBOID);
		glBufferData(GL_ARRAY_BUFFER, ColladaLoader.vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, normsVBOID);
		glBufferData(GL_ARRAY_BUFFER, ColladaLoader.normals, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, tcVBOID);
		glBufferData(GL_ARRAY_BUFFER, ColladaLoader.texCoords, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		ColladaLoader.flushData();
	}
	
	private void loadFromOBJ(String fName) {
		vaoID = glGenVertexArrays();
		try {
			OBJLoader.readOBJ("src/main/resources/assets/models/" + fName, faceType);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.print("Could not find OBJFile: " + fName);
		}
		glBindVertexArray(vaoID);
		int vertsVBOID = glGenBuffers();
		int normsVBOID = glGenBuffers();
		int tcVBOID = glGenBuffers();
		int indVBOID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indVBOID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, OBJLoader.indices, GL_STATIC_DRAW);
		this.indicyCount = OBJLoader.indices.length;
		
		glBindBuffer(GL_ARRAY_BUFFER, vertsVBOID);
		glBufferData(GL_ARRAY_BUFFER, OBJLoader.vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, normsVBOID);
		glBufferData(GL_ARRAY_BUFFER, OBJLoader.normals, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); //TODO: change normalized bool for fun to see what happens to lighting
		
		glBindBuffer(GL_ARRAY_BUFFER, tcVBOID);
		glBufferData(GL_ARRAY_BUFFER, OBJLoader.texCoords, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		OBJLoader.flushData();
	}
	public void setVAOId(int id) {
		this.vaoID = id;
	}
	public void setIndicyCount(int count) {
		this.indicyCount = count;
	}
	public int getFaceType() {
		return this.faceType;
	}
	public int getIndicyCount() {
		return this.indicyCount;
	}
	
	public int getVAO() {
		return this.vaoID;
	}

	public void setTexture(ModelTexture tex){
		this.texture = tex;
	}

	public ModelTexture getTexture(){
		return this.texture;
	}

	public boolean hasTexture(){
		return texture != null;
	}
	
	public void cleanUp() {
		if(hasTexture()) {
			glDeleteTextures(texture.getId());
		}
		glDeleteVertexArrays(vaoID);
	}
	
	public static Model getRandomTerrainModel(int size, int vertexCount, int amplitude, int seed) {
		Model model = new Model(GL_TRIANGLES);
		int vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		int vertsVBOID = glGenBuffers();
		int normsVBOID = glGenBuffers();
		int tcVBOID = glGenBuffers();
		int indVBOID = glGenBuffers();
		int width = (int) Math.sqrt((double)vertexCount);
		float offset = size/width;

		
		ArrayList<Integer> indicesList = new ArrayList<>();
		int[] indices = new int[((width - 1)*(width-1))*6];
		for(int col = 1; col < width; col ++) { // Loop through every (quad) face and add too indicesList. (Drawing two triangles each )
			for(int row = 1; row < width; row++) {
				int index = (row*width) + col;
				int leftIndex =	index - 1;
				int bottomLeftIndex = ((row - 1) * width) + col - 1;
				int bottomIndex = bottomLeftIndex + 1;
				indicesList.add(index);
				indicesList.add(leftIndex);
				indicesList.add(bottomLeftIndex);
				indicesList.add(index);
				indicesList.add(bottomLeftIndex);
				indicesList.add(bottomIndex);
			}
		}
		for(int i = 0; i < indices.length; i++) {
			indices[i] = indicesList.get(i);
		}
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indVBOID);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		
		
		float[] vertices = new float[vertexCount*3];
		for(int row = 0; row < width; row++) { // Generate vertex data.
			for(int col = 0; col < width; col++) {
				float x = offset * col;
				float z = offset * row * -1;
				int index = (row * width) + col;
				//float y = getSeedHeight(x, z, seed) * amplitude;
				//float y = getSmoothHeight(x, z, offset, seed) * amplitude;
				float y = getCosInterpolatedHeight(x, z, offset, seed) * amplitude;
				vertices[index * 3] = x;
				vertices[index * 3 + 1] = y;
				vertices[index * 3 + 2] = z;
			}
		}
		glBindBuffer(GL_ARRAY_BUFFER, vertsVBOID);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
		
		float[] normals = new float[vertexCount*3];
		for(int row = 0; row < width; row++) {
			for(int col = 0; col < width; col++) {
				float heightL;
				float heightR;
				float heightU;
				float heightD;
				int index = ((row*width) + col) * 3;//Of current vector x value.
				if(col==width-1) {
					heightR = 0;
				}else {
					heightR = vertices[index + 4];
				}
				if(col==0) {
					heightL = 0;
				}else {
					heightL = vertices[index - 2];
				}
				if(row==width-1) {
					heightU = 0;
				}else {
					heightU = vertices[index + (width*3) + 1];
				}
				if(row==0) {
					heightD = 0;
				}
				else {
					heightD = vertices[index - (width*3) + 1];
				}
				normals[index] = heightL - heightR;
				normals[index+1] = 2;
				normals[index+2] = heightD - heightU;
			} 
		}
		glBindBuffer(GL_ARRAY_BUFFER, normsVBOID);
		glBufferData(GL_ARRAY_BUFFER, normals, GL_STATIC_DRAW);
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0); //TODO: change normalized bool for fun to see what happens to lighting
		
		float[] texCoords = new float[vertexCount*2];
		for(int row = 0; row < width; row++) { // Loop through texCoords array and apply tex coords quad by quad.
			for(int col = 0; col < width; col++) {
				int index = (row*width) + col;
				texCoords[index*2] = col;
				texCoords[index*2+1] = row;
			}
		}
			
		
			
		glBindBuffer(GL_ARRAY_BUFFER, tcVBOID);
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		
		model.setVAOId(vaoID);
		model.setIndicyCount(indices.length);
		return model;
	}
	
	// Returns between -1 and 1
	public static float getSeedHeight(float x, float z, int seed) { 
		Random random = new Random();
		random.setSeed((long) ((x*1024 * z*1024) + seed));
		return (random.nextFloat() * 2) - 1;
	}
	
	public static float getSmoothHeight(float x, float z, float offset, int seed) {
		float hUp = getSeedHeight(x, z - offset, seed);
		float hDown = getSeedHeight(x, z + offset, seed);
		float hRight = getSeedHeight(x + offset, z, seed);
		float hLeft = getSeedHeight(x - offset, z, seed);
		float averageSides = (hUp + hDown + hRight + hLeft) / 8;
		float hUL = getSeedHeight(x - offset, z - offset, seed);
		float hUR = getSeedHeight(x + offset, z - offset, seed);
		float hLL = getSeedHeight(x - offset, z + offset, seed);
		float hLR = getSeedHeight(x + offset, z + offset, seed);
		float averageCorners = (hUL + hUR + hLL + hLR) / 16;
		float center = getSeedHeight(x, z, seed) / 4;
		float average = (averageSides + averageCorners + center);
		return average;
	}
	public static float getCosInterpolatedHeight(float x, float z, float offset, int seed) {
		float fracX = (x % offset);
		float fracZ = (z % offset);
		float gridX = x - fracX;
		float gridZ = z - fracZ;
		float hTopLeft = getSmoothHeight(gridX, gridZ - offset, offset, seed);
		float hTopRight = getSmoothHeight(gridX + offset, gridZ - offset, offset, seed);
		float hBottomLeft = getSmoothHeight(gridX, gridZ, offset, seed);
		float hBottomRight = getSmoothHeight(gridX + offset, gridZ, offset, seed);
		float hTop = interpolate(hTopLeft, hTopRight, fracX/offset);
		float hBottom = interpolate(hBottomLeft, hBottomRight, fracX/offset);
		float height = interpolate(hTop, hBottom, fracZ/offset);
		return height;
		
	}
	private static float interpolate(float a, float b, float distance) {
		double theta = distance * Math.PI;
		float value = (float)(1f - Math.cos(theta)) * 0.5f;
		return a * (1-value) + b * value;
	}

}
