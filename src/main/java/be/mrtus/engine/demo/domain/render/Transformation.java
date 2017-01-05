package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

	private final Matrix4f emptyMatrix;
	private final Matrix4f modelViewMatrix;
	private final Matrix4f projectionMatrix;
	private final float rad = (float)(180 / Math.PI);
	private final Matrix4f viewMatrix;

	public Transformation() {
		this.projectionMatrix = new Matrix4f();
		this.modelViewMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.emptyMatrix = new Matrix4f();
	}

	public Matrix4f getModelViewMatrix(Transform transform) {
		Vector3f rotation = transform.getRotation();
		this.modelViewMatrix.identity();
		this.modelViewMatrix.translate(transform.getPosition());
		this.modelViewMatrix.rotateX(-rotation.x / this.rad);
		this.modelViewMatrix.rotateY(-rotation.y / this.rad);
		this.modelViewMatrix.rotateZ(-rotation.z / this.rad);
		this.modelViewMatrix.scale(transform.getScale());
		this.emptyMatrix.identity();
		return this.viewMatrix.mul(this.modelViewMatrix, this.emptyMatrix);
	}

	public Matrix4f getProjectionMatrix() {
		return this.projectionMatrix;
	}

	public Matrix4f getViewMatrix() {
		return this.viewMatrix;
	}

	public void setViewMatrix(Camera camera) {
		Vector3f position = camera.getTransform().getPosition();
		Vector3f rotation = camera.getTransform().getRotation();
		this.viewMatrix.identity();
		this.viewMatrix.rotate(rotation.x / this.rad, 1, 0, 0);
		this.viewMatrix.rotate(rotation.y / this.rad, 0, 1, 0);
//		this.viewMatrix.rotate(rotation.z / this.rad, 0, 0, 1);
		this.viewMatrix.translate(-position.x, -position.y, -position.z);
	}

	public void setProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		this.projectionMatrix.identity();
		this.projectionMatrix.perspective(fov, width / height, zNear, zFar);
	}
}
