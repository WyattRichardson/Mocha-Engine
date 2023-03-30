package richardson.wyatt.game_entities.model;
import static org.lwjgl.opengl.GL30.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;

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
	
	public static Model getRandomTerrainModel(int size, int vertexCount, int amplitude) {
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
		for(int col = 0; col < width; col ++) { // Loop through every face and add too indicesList.
			for(int row = 1; row < width; row++) {
				int topRightIndex = (row*width) + col;
				int topLeftIndex = topRightIndex + 1;
				int bottomLeftIndex = ((row - 1) * width) + col + 1;
				int bottomRightIndex = bottomLeftIndex - 1;
				indicesList.add(topRightIndex);
				indicesList.add(topLeftIndex);
				indicesList.add(bottomLeftIndex);
				indicesList.add(topRightIndex);
				indicesList.add(bottomLeftIndex);
				indicesList.add(bottomRightIndex);
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
				float x = offset * col * -1;
				float y = (float)Math.random() / 2;
				float z = offset * row * -1;
				int index = (row * width) + col;
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
				int index = ((row * width) + col) * 3;//Of current vector x value.
				if(col==width-1) {
					heightL = 0;
				}else {
					heightL = vertices[index + 4];
				}
				if(col==0) {
					heightR = 0;
				}else {
					heightR = vertices[index - 2];
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
		glBindBuffer(GL_ARRAY_BUFFER, tcVBOID);
		glBufferData(GL_ARRAY_BUFFER, texCoords, GL_STATIC_DRAW);
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 0, 0);
		
		glBindBuffer(GL_ARRAY_BUFFER, 0);
		glBindVertexArray(0);
		
		
		model.setVAOId(vaoID);
		model.setIndicyCount(indices.length);
		return model;
	}
	

}
