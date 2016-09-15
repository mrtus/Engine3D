package be.mrtus.engine.domain.scene;

import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.component.EntityController;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraController extends EntityController<Camera> {

	private final Keyboard keyboard;
	private final Mouse mouse;

	public CameraController(Keyboard keyboard, Mouse mouse) {
		this.keyboard = keyboard;
		this.mouse = mouse;
	}

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		Vector3f rotation = this.entity.getPosition();
		rotation.x += offsetX * 0.1;
		rotation.y += offsetY * 0.1;
		rotation.z += offsetZ * 0.1;
//		System.out.println("x " + rotation.x + " y " + rotation.y);
	}

	@Override
	public void update() {
		Vector2f deltaPos = this.mouse.getDeltaPos();
		this.moveRotation(deltaPos.x, deltaPos.y, 0);
	}
}
