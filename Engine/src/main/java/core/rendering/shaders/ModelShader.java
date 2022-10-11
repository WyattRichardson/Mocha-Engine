package core.rendering.shaders;
import static org.lwjgl.opengl.GL30.*;

import core.application.Scene;
import core.utils.Math;
public class ModelShader extends ShaderProgram {
	
	private int programID;
	public static final int MAX_LIGHTS = 4;
	public ModelShader(String vertPath, String fragPath) {
		super();
		this.programID = glCreateProgram();
		int vertID = loadShader(vertPath, GL_VERTEX_SHADER);
		int fragID = loadShader(fragPath, GL_FRAGMENT_SHADER);
		glAttachShader(programID, vertID);
		glAttachShader(programID, fragID);
		glLinkProgram(programID);
		glValidateProgram(programID);
		glUseProgram(programID);
		getUniformLocations();
		glUseProgram(0);
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		
	}

	
	
	public int getID() {
		return this.programID;
	}



	@Override
	public void getUniformLocations() {
		uniformLocations.put("projectionMatrix", glGetUniformLocation(this.programID, "projectionMatrix"));
		float[] projectionMat = new float[16];
		Math.createProjectionMatrix(Scene.currentVidMode).get(projectionMat);
		glUniformMatrix4fv(uniformLocations.get("projectionMatrix"), false, projectionMat);
		
		uniformLocations.put("viewMatrix", glGetUniformLocation(this.programID, "viewMatrix"));
		
		
		uniformLocations.put("transformationMatrix", glGetUniformLocation(this.programID, "transformationMatrix"));
		
		for(int i = 0; i < MAX_LIGHTS; i++) {
			uniformLocations.put("lightPositions[" + i + "]", glGetUniformLocation(this.programID, "lightPositions[" + i + "]"));
		}
		for(int i = 0; i < MAX_LIGHTS; i++) {
			uniformLocations.put("lightColors[" + i + "]", glGetUniformLocation(this.programID, "lightColors[" + i + "]"));
		}
	}
	

}
