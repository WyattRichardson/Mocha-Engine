package core.gameobjects.entity;


import org.joml.Vector3f;

public class Transform extends EntityComponent{
	
	public Vector3f position;
	public Vector3f rotation;
	public float scale;
	
	public Transform(float x, float y, float z, float rx, float ry, float rz, float scale) {
		super();
		position = new Vector3f(x, y, z);
		rotation = new Vector3f(rx, ry, rz);
		this.scale = scale;
	}
	public Transform() {
		super();
	}
	@Override
	public void setComponentType() {
		type = EntityComponent.TYPE_TRANSFORM;
	};
}
