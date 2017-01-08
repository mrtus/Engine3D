package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.scene.Camera;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.apache.commons.math3.util.FastMath;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

public class Transformation {

	private final Matrix4f emptyMatrix;
	private final Matrix4f modelViewMatrix;
	private final Matrix4f projectionMatrix;
	private final Matrix4f viewMatrix;

	public Transformation() {
		this.projectionMatrix = new Matrix4f();
		this.modelViewMatrix = new Matrix4f();
		this.viewMatrix = new Matrix4f();
		this.emptyMatrix = new Matrix4f();
	}

	public Matrix4f getModelViewMatrix(Transform transform) {
		Vector3f rotation = transform.getRotation();
		this.modelViewMatrix.identity()
				.translate(transform.getPosition())
				.rotateXYZ((float)FastMath.toRadians(rotation.x), (float)FastMath.toRadians(rotation.y), (float)FastMath.toRadians(rotation.z))
				.scale(transform.getScale());
		return this.viewMatrix.mul(this.modelViewMatrix, this.emptyMatrix.identity());
	}

	public Matrix4f getModelViewMatrix(Vector2i position) {
		this.modelViewMatrix.identity()
				.translate(position.x, 0, position.y);
		return this.viewMatrix.mul(this.modelViewMatrix, this.emptyMatrix.identity());
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
		this.viewMatrix.identity()
				.rotateXYZ((float)FastMath.toRadians(rotation.x), (float)FastMath.toRadians(rotation.y), 0)
				.translate(-position.x, -position.y, -position.z);
	}

	public void setProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		this.projectionMatrix.identity().perspective(fov, width / height, zNear, zFar);
	}
}
