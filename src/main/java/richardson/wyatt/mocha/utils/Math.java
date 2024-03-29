package richardson.wyatt.mocha.utils;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

import richardson.wyatt.mocha.game_entities.entity.Transform;

public final class Math {
	private static final float FOV = 90;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	public static float cosInterpolate(float a, float b, float weight) {
		double theta = weight * org.joml.Math.PI;
		float value = (float)(1f - org.joml.Math.cos(theta)) * 0.5f;
		return a * (1-value) + b * value;
	}
	
	public static float linearInterpolate(float a, float b, float weight) {
		float difference = b - a;
		float value = difference*weight;
		return a + value;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, Vector3f rotation, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation, matrix);
		float rx = rotation.x;
		float ry = rotation.y;
		float rz = rotation.z;
		matrix.rotate((float) java.lang.Math.toRadians(rx), new Vector3f(1,0,0), matrix);
		matrix.rotate((float) java.lang.Math.toRadians(ry), new Vector3f(0,1,0), matrix);
		matrix.rotate((float) java.lang.Math.toRadians(rz), new Vector3f(0,0,1), matrix);
		matrix.scale(new Vector3f(scale, scale, scale), matrix);
		return matrix;
		
	}
	
	public static Matrix4f createProjectionMatrix(GLFWVidMode vidmode){
		Matrix4f projectionMatrix;
        float aspectRatio = (float) vidmode.width() / (float) vidmode.height();
        float y_scale = (float) ((1f / java.lang.Math.tan(java.lang.Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
        return projectionMatrix;
	}
	public static Matrix4f createViewMatrix(Transform camTransform){
		  Matrix4f viewMatrix = new Matrix4f();
		  viewMatrix.identity();
		  viewMatrix.rotate((float) java.lang.Math.toRadians(camTransform.getRotation().x), new Vector3f(1,0,0), viewMatrix);
		  viewMatrix.rotate((float) java.lang.Math.toRadians(camTransform.getRotation().y), new Vector3f(0,1,0), viewMatrix);
		  viewMatrix.rotate((float) java.lang.Math.toRadians(camTransform.getRotation().z), new Vector3f(0,0,1), viewMatrix);
		  Vector3f cameraPos = camTransform.getPosition();
		  Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		  viewMatrix.translate(negativeCameraPos, viewMatrix);
		  return viewMatrix;
	}
}
