package richardson.wyatt.rendering;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import richardson.wyatt.application.Camera;
import richardson.wyatt.application.Scene;
import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.entity.EntityController;
import richardson.wyatt.game_entities.entity.Transform;
import richardson.wyatt.game_entities.entity.EntityComponent.Type;
import richardson.wyatt.game_entities.lighting.Light;
import richardson.wyatt.game_entities.model.Model;
import richardson.wyatt.game_entities.textures.ModelTexture;
import richardson.wyatt.rendering.shaders.ModelShader;
import richardson.wyatt.utils.Math;

import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL30.*;

public final class Renderer {

	public Map<Model, ArrayList<Entity>> entitiesWithModels;
	public List<Entity> entitiesWithoutModels;

	private ModelShader modelShader;
	private Camera activeCam;
	private Scene scene;

	public Renderer() {
		
		entitiesWithModels = new HashMap<Model, ArrayList<Entity>>(); 
		entitiesWithoutModels = new ArrayList<Entity>();
		modelShader = new ModelShader("src/main/resources/assets/shaders/modelVertShader.txt",
		"src/main/resources/assets/shaders/modelFragShader.txt");
		
	}

	public void render(float dt, Scene scene) {
		this.scene = scene;
		this.entitiesWithModels = scene.getEntitiesWithModels();
		this.entitiesWithoutModels = scene.getEntitiesWithoutModels();
		this.activeCam = scene.activeCamera;
		
		glUseProgram(modelShader.getID());
		
		if(activeCam.hasController()) {
			EntityController camController = (EntityController) activeCam.getComponentByType(Type.CONTROLLER);
			camController.tick(dt);
		}
		
		tickEntitiesWithoutModels(dt);
		renderEntitiesWithModels(dt);
		
		glUseProgram(0);

	}

	public void tickEntitiesWithoutModels(float dt) {
		for (Entity entity : entitiesWithoutModels) {
			if (entity.hasController()) {
				EntityController controller = (EntityController) entity.getComponentByType(Type.CONTROLLER);
				controller.tick(dt);
			}
			if(entity.hasLight()){//TEST THIS
				Light light = (Light) entity.getComponentByType(Type.LIGHT);
				int uniformIndex = light.getUniformIndex();
				if(entity.hasTransform()){
					Transform transform = (Transform) entity.getComponentByType(Type.TRANSFORM);
					glUniform3f(modelShader.uniformLocations.get("lightPositions[" + uniformIndex + "]"), transform.getPosition().x, transform.getPosition().y, transform.getPosition().z);
					glUniform3f(modelShader.uniformLocations.get("lightColors[" + uniformIndex + "]"), light.getColor().x, light.getColor().y, light.getColor().z);
				}else{
					System.err.println("ENTITY WITH LIGHT: " + entity.getId() + " HAS NO TRANSFORM!");
					throw new IllegalStateException();
				}
			}
		}
	}

	public void renderEntitiesWithModels(float dt) throws NullPointerException{
		for (Model model : entitiesWithModels.keySet()) {

			int vaoID = model.getVAO();

			glBindVertexArray(vaoID);

			glEnableVertexAttribArray(0);
			glEnableVertexAttribArray(1);
			glEnableVertexAttribArray(2);

			if(model.hasTexture()){
				ModelTexture texture = model.getTexture();
				glActiveTexture(texture.getUnit());
				texture.bind();
			}
			List<Entity> batch = entitiesWithModels.get(model);
			
			for (Entity entity: batch) {
				if(!entity.hasTransform()){
					System.err.println("ENTITY: " + entity.getId() + " DOES NOT HAVE A TRANSFORM AND WAS SENT TO BE RENDERED!");
					throw new NullPointerException();
				}
				Transform transform = (Transform) entity.getComponentByType(Type.TRANSFORM);	
				if (isInFOV(transform.getPosition())) {
					if (entity.hasController()) {
						EntityController controller = (EntityController) entity.getComponentByType(Type.CONTROLLER);
						controller.tick(dt);
					}
					float[] modelTransformMat = new float[16];
					float[] viewMatrix = new float[16];
					Math.createTransformationMatrix(transform.getPosition(), transform.getRotation(), transform.getScale()).get(modelTransformMat);
					Math.createViewMatrix((Transform) activeCam.getComponentByType(Type.TRANSFORM)).get(viewMatrix);
					glUniformMatrix4fv(modelShader.uniformLocations.get("viewMatrix"), false, viewMatrix);
					glUniformMatrix4fv(modelShader.uniformLocations.get("transformationMatrix"), false, modelTransformMat);
					glDrawElements(model.getFaceType(), model.getIndicyCount(), GL_UNSIGNED_INT, 0);
				}

			}
			glDisableVertexAttribArray(0);
			glDisableVertexAttribArray(1);
			glDisableVertexAttribArray(2);
			glBindVertexArray(0);

			if(model.hasTexture()){
				model.getTexture().unbind();
			}

		}
	}

		

	public boolean isInFOV(Vector3f position) {
		Transform camTransform = (Transform) activeCam.getComponentByType(Type.TRANSFORM);
		Vector3f toObjVec =  new Vector3f(position).sub(camTransform.getPosition()).normalize();
		float camNormX = (float)org.joml.Math.sin(org.joml.Math.toRadians(camTransform.getRotation().y));
		float camNormY = (float)org.joml.Math.sin(org.joml.Math.toRadians(-camTransform.getRotation().x));
		float camNormZ = (float)org.joml.Math.cos(org.joml.Math.toRadians(camTransform.getRotation().y)) * -1;
		Vector3f camNorm = new Vector3f(camNormX,camNormY,camNormZ);
		float dotProd = camNorm.dot(toObjVec);
		
		if(dotProd < 0.75) {
			return false;
		} else {
			return true;
		}
//		System.out.print(camNorm.x + "    ");
//		System.out.print(camNorm.y + "    ");
//		System.out.print(camNorm.z + "    ");
//		System.out.println();
	}


	public void cleanUp() {
		glDeleteProgram(modelShader.getID());

	}

}