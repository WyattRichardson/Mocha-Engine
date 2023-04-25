package richardson.wyatt.rendering.shaders;
import static org.lwjgl.opengl.GL30.*;


import richardson.wyatt.application.Window;
import richardson.wyatt.utils.Math;
public class ModelShader extends ShaderProgram {
	
	public static final int MAX_LIGHTS = 4;

	public static final int MAX_TEXTURES = 4;

	public ModelShader(String vertPath, String fragPath) {
		super();
		programId = glCreateProgram();
		int vertID = loadShader(vertPath, GL_VERTEX_SHADER);
		int fragID = loadShader(fragPath, GL_FRAGMENT_SHADER);
		glAttachShader(programId, vertID);
		glAttachShader(programId, fragID);
		glLinkProgram(programId);
		glValidateProgram(programId);
		glUseProgram(programId);
		getUniformLocations();
		glUseProgram(0);
		glDeleteShader(vertID);
		glDeleteShader(fragID);
		
	}

	@Override
	public void getUniformLocations() {
		uniformLocations.put("projectionMatrix", glGetUniformLocation(this.programId, "projectionMatrix"));
		float[] projectionMat = new float[16];
		Math.createProjectionMatrix(Window.currentVidMode).get(projectionMat);
		glUniformMatrix4fv(uniformLocations.get("projectionMatrix"), false, projectionMat);
		
		uniformLocations.put("viewMatrix", glGetUniformLocation(this.programId, "viewMatrix"));
		uniformLocations.put("transformationMatrix", glGetUniformLocation(this.programId, "transformationMatrix"));
		
		for(int i = 0; i < MAX_LIGHTS; i++) {
			uniformLocations.put("lightPositions[" + i + "]", glGetUniformLocation(this.programId, "lightPositions[" + i + "]"));
		}
		for(int i = 0; i < MAX_LIGHTS; i++) {
			uniformLocations.put("lightColors[" + i + "]", glGetUniformLocation(this.programId, "lightColors[" + i + "]"));
		}
		for(int i = 0; i < MAX_TEXTURES; i++) {
			uniformLocations.put("texture" + i, glGetUniformLocation(this.programId, "texture" + i));
			glUniform1i(uniformLocations.get("texture" + i), i);
		}
	}
	

}
