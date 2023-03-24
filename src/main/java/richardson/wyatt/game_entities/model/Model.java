package richardson.wyatt.game_entities.model;
import static org.lwjgl.opengl.GL30.*;
import java.io.FileNotFoundException;

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

}
