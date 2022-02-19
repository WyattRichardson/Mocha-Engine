package main;

import core.gameobjects.camera.Camera;
import core.gameobjects.camera.CameraController;

public class PlayerCameraController extends CameraController{

	public PlayerCameraController(Camera camera) {
		super(camera);
	}

	@Override
	public void tick(float dt) {
		updateTransform(dt);
		
	}

	@Override
	public void updateTransform(float dt) {
		// TODO Auto-generated method stub
		
	}

}
