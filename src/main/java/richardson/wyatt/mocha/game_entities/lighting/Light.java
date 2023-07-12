package richardson.wyatt.mocha.game_entities.lighting;
import org.joml.Vector3f;

import richardson.wyatt.mocha.game_entities.entity.Entity;
import richardson.wyatt.mocha.game_entities.entity.EntityComponent;
public class Light extends EntityComponent {
	
	private Vector3f color;
	public static int modelLightIndex = 0;
	private int uniformIndex;
	public Light(Vector3f color) {
		super(Type.LIGHT);
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
