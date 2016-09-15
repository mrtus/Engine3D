package be.mrtus.engine.demo.domain.render;

import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Transformation {

	private final Matrix4f projectionMatrix;
	private final Matrix4f worldMatrix;

	public Transformation() {
		this.projectionMatrix = new Matrix4f();
		this.worldMatrix = new Matrix4f();
	}

	public final Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		this.projectionMatrix.identity();
		this.projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return this.projectionMatrix;
	}

	public Matrix4f getWorldMatrix(Transform transform) {
		Vector3f rotation = transform.getRotation();
		this.worldMatrix.identity();
		this.worldMatrix.translate(transform.getPosition());
		this.worldMatrix.rotateX((float)Math.toRadians(rotation.x));
		this.worldMatrix.rotateY((float)Math.toRadians(rotation.y));
		this.worldMatrix.rotateZ((float)Math.toRadians(rotation.z));
		this.worldMatrix.scale(transform.getScale());
		return this.worldMatrix;
	}
}
