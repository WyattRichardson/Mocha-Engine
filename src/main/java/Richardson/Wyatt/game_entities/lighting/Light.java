package Richardson.Wyatt.game_entities.lighting;
import org.joml.Vector3f;

import Richardson.Wyatt.game_entities.entity.Entity;
public class Light extends Entity {
	
	private Vector3f color;
	public static int modelLightIndex = 0;
	private int uniformIndex;
	public Light(String entityId, Vector3f color) {
		super(entityId);
		this.color = color;
		uniformIndex = modelLightIndex;
		modelLightIndex += 1;
	}
	
	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public int getUniformIndex() {
		return uniformIndex;
	}
	

}
