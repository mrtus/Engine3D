package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

	private final Matrix4f projectionMatrix;
	private final Matrix4f viewMatrix;
	private final Matrix4f modelViewMatrix;

	public Transformation() {
		this.projectionMatrix = new Matrix4f();
		this.modelViewMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
	}

	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		this.projectionMatrix.identity();
		this.projectionMatrix.perspective(fov, width / height, zNear, zFar);
		return this.projectionMatrix;
	}

	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f position = camera.getTransform().getPosition();
		Vector3f rotation = camera.getTransform().getRotation();
		this.viewMatrix.identity();
		this.viewMatrix.rotate((float)Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
		this.viewMatrix.rotate((float)Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		this.viewMatrix.translate(-position.x, -position.y, -position.z);
		return this.viewMatrix;
	}

	public Matrix4f getModelViewMatrix(Transform transform, Matrix4f viewMatrix) {
		Vector3f rotation = transform.getRotation();
		this.modelViewMatrix.identity();
		this.modelViewMatrix.translate(transform.getPosition());
		this.modelViewMatrix.rotateX((float)Math.toRadians(-rotation.x));
		this.modelViewMatrix.rotateY((float)Math.toRadians(-rotation.y));
		this.modelViewMatrix.rotateZ((float)Math.toRadians(-rotation.z));
		this.modelViewMatrix.scale(transform.getScale());
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(this.modelViewMatrix);
	}
}
