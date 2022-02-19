package core.utils;


import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFWVidMode;

public final class Math {
	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
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
/*	public static Matrix4f createViewMatrix(Camera camera){
		  Matrix4f viewMatrix = new Matrix4f();
		  viewMatrix.identity();
		  viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getRx()), new Vector3f(1,0,0), viewMatrix);
		  viewMatrix.rotate((float) java.lang.Math.toRadians(camera.getRy()), new Vector3f(0,1,0), viewMatrix);
		  Vector3f cameraPos = camera.getPosition();
		  Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
		  viewMatrix.translate(negativeCameraPos, viewMatrix);
		  return viewMatrix;
	}*/
}
