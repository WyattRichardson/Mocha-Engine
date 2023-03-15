package richardson.wyatt.rendering;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector3f;

import richardson.wyatt.game_entities.entity.Entity;
import richardson.wyatt.game_entities.entity.EntityController;
import richardson.wyatt.game_entities.entity.Transform;
import richardson.wyatt.game_entities.entity.EntityComponent.Type;
import richardson.wyatt.game_entities.lighting.Light;
import richardson.wyatt.game_entities.model.Model;
import richardson.wyatt.game_entities.textures.ModelTexture;
import richardson.wyatt.rendering.shaders.ModelShader;
import richardson.wyatt.utils.Math;

import static org.lwjgl.opengl.GL30.*;

public final class EntityRenderer {

	public Map<Model, ArrayList<Entity>> entities_with_models;
	public List<Entity> entities_without_models;

	private ModelShader modelShader;

	public EntityRenderer() { //TODO: implement camera functionality!
		
		entities_with_models = new HashMap<Model, ArrayList<Entity>>(); //All 
		entities_without_models = new ArrayList<Entity>();
		modelShader = new ModelShader("src/main/resources/assets/shaders/modelVertShader.txt",
		"src/main/resources/assets/shaders/modelFragShader.txt");
	}

	public void render(float dt) {

		glUseProgram(modelShader.getID());
		
		tickEntitiesWithoutModels(dt);
		renderEntitiesWithModels(dt);
		
		glUseProgram(0);

	}

	public void tickEntitiesWithoutModels(float dt) throws NullPointerException {
		for (Entity entity : entities_without_models) {
			if (entity.hasController()) {
				EntityController controller = (EntityController) entity.getComponentByType(Type.CONTROLLER);
				controller.tick(dt);
				if(entity.getClass().getSimpleName().equals("Light")){//TEST THIS
					Light light = (Light) entity;
					int uniformIndex = light.getUniformIndex();
					if(light.hasTransform()){
						Transform transform = (Transform) light.getComponentByType(Type.TRANSFORM);
						glUniform3f(modelShader.uniformLocations.get("lightPositions[" + uniformIndex + "]"), transform.getPosition().x, transform.getPosition().y, transform.getPosition().z);
						glUniform3f(modelShader.uniformLocations.get("lightColors[" + uniformIndex + "]"), light.getColor().x, light.getColor().y, light.getColor().z);
					}else{
						System.err.println("LIGHT: " + light.getId() + " DOES NOT HAVE A TRANSFORM AND WAS SENT TO BE TICKED!");
						throw new NullPointerException();
					}
				}
			}else{
				System.err.println("WARNING! ENTITY: " + entity.getId() + " DOES NOT HAVE CONTROLLER AND WAS SENT TO BE TICKED!");
			}
		}
	}

	public void renderEntitiesWithModels(float dt) throws NullPointerException{
		for (Model model : entities_with_models.keySet()) {

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
			List<Entity> batch = entities_with_models.get(model);
			
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
					Math.createTransformationMatrix(transform.getPosition(), transform.getRotation(), transform.getScale())
					.get(modelTransformMat);
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
		return true; //me TODO: implent
	}

	public void addEntity(Entity entity) {
		if(entity.hasModel()){
			Model model = (Model) entity.getComponentsByType(Type.MODEL).get(0);
			if(!entities_with_models.containsKey(model)){
				entities_with_models.put(model, new ArrayList<Entity>());
			}
			entities_with_models.get(model).add(entity);
		}else{
			entities_without_models.add(entity);
		}
	}

	public void cleanUp() {
		glDeleteProgram(modelShader.getID());
		// TODO: cycle through all models and call a cleanup function defined within
		// Model class
	}

}