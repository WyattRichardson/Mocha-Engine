package core.gameobjects.lighting;

import org.joml.Vector3f;

import core.gameobjects.entity.EntityComponent;
import core.gameobjects.entity.Transform;

public class Light extends EntityComponent {
	
	public Transform transform = new Transform();
	private Vector3f color;
	public static int modelLightIndex = 0;
	private int index;
	public Light() {
		super();
		transform.position = new Vector3f(0,0,-1);
		transform.rotation = new Vector3f(0,0,0);
		color = new Vector3f(1,1,1);
		index = modelLightIndex;
		modelLightIndex += 1;
	}
	public Light(Vector3f position, Vector3f rotation, Vector3f color) {
		super();
		this.transform.position = position;
		this.transform.rotation = rotation;
		this.color = color;
		index = modelLightIndex;
		modelLightIndex += 1;
	}

	public Vector3f getPosition() {
		return transform.position;
	}

	public void setPosition(Vector3f position) {
		transform.position = position;
	}

	public Vector3f getRotation() {
		return transform.rotation;
	}

	public void setRotation(Vector3f rotation) {
		transform.rotation = rotation;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	@Override
	public void setComponentType() {
		type = EntityComponent.TYPE_LIGHT;
	}
	public int getIndex() {
		return index;
	}
	

}
