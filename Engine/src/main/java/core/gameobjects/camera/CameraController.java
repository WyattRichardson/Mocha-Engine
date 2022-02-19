package core.gameobjects.camera;

public abstract class CameraController {

	
	public float horizontalSpeed;
	
	public float verticalSpeed;
	
	public Camera camera;
	
	
	
	public CameraController(Camera camera) {
		this.camera = camera;
	}
	
	public abstract void tick(float dt);
	
	public abstract void updateTransform(float dt);
	
	
}
