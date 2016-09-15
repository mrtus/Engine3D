package be.mrtus.engine.demo.domain.entity.component;

import be.mrtus.engine.domain.scene.entity.component.EntityController;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Vector3f;

public class SpinningEntityController extends EntityController {

	private final Vector3f angleSpeed;

	public SpinningEntityController(float angleSpeed) {
		this.angleSpeed = new Vector3f(angleSpeed, angleSpeed, angleSpeed);
	}

	public SpinningEntityController(Vector3f angleSpeed) {
		this.angleSpeed = angleSpeed;
	}

	@Override
	public void update() {
		Transform transform = this.entity.getTransform();
		Vector3f rotation = new Vector3f();
		rotation.x = transform.getRotation().x + this.angleSpeed.x;
		rotation.y = transform.getRotation().x + this.angleSpeed.y;
		rotation.z = transform.getRotation().x + this.angleSpeed.z;
		if(rotation.x > 360) {
			rotation.x -= 360;
		}
		if(rotation.y > 360) {
			rotation.y -= 360;
		}
		if(rotation.z > 360) {
			rotation.z -= 360;
		}
		transform.getRotation().set(rotation.x, rotation.y, rotation.z);
	}
}
