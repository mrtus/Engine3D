package be.mrtus.engine.domain.scene;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.component.EntityController;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraController extends EntityController<Camera> {

	private final double RAD = 180 / Math.PI;
	private final Keyboard keyboard;
	private final Mouse mouse;
	private final Vector3f move = new Vector3f();
	private boolean noclip = false;
	private final float speed = 0.2f;
	private int sprint = 1;

	public CameraController(Keyboard keyboard, Mouse mouse) {
		this.keyboard = keyboard;
		this.mouse = mouse;
	}

	public void movePosition(Scene scene, float offsetX, float offsetY, float offsetZ) {
		Vector3f position = this.entity.getPosition();
		Vector3f rotation = this.entity.getTransform().getRotation();
		if(offsetZ != 0) {
			position.x += (float)Math.sin(rotation.y / this.RAD) * -offsetZ;
			position.z += (float)Math.cos(rotation.y / this.RAD) * offsetZ;
		}
		if(offsetX != 0) {
			position.x += (float)Math.sin(rotation.y - 90 / this.RAD) * -offsetX;
			position.z += (float)Math.cos(rotation.y - 90 / this.RAD) * offsetX;
		}
		if(this.noclip) {
			position.y += offsetY;
		} else {
			position.y = scene.calculateTerrainHeight(position);
		}
	}

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		Vector3f rotation = this.entity.getTransform().getRotation();
		rotation.y += offsetX * 0.1;
		rotation.x += offsetY * 0.1;
//		rotation.z += offsetZ * 0.1;
	}

	@Override
	public void update(Scene scene) {
		if(this.keyboard.isKeyPressed("reset_pos")) {
			this.entity.reset();
		}
		if(this.keyboard.isKeyPressed("sprint")) {
			this.sprint = 5;
		} else {
			this.sprint = 1;
		}
		if(this.keyboard.isKeyPressed("noclip")) {
			this.noclip = !this.noclip;
		}

		Vector2f deltaPos = this.mouse.getDeltaPos();
		this.moveRotation(deltaPos.x, deltaPos.y, 0);

		this.move.set(0, 0, 0);
		if(this.keyboard.isKeyPressed("forward")) {
			this.move.z = -1;
		} else if(this.keyboard.isKeyPressed("backward")) {
			this.move.z = 1;
		}
		if(this.keyboard.isKeyPressed("left")) {
			this.move.x = -1;
		} else if(this.keyboard.isKeyPressed("right")) {
			this.move.x = 1;
		}
		if(this.noclip) {
			if(this.keyboard.isKeyPressed("down")) {
				this.move.y = -1;
			} else if(this.keyboard.isKeyPressed("up")) {
				this.move.y = 1;
			}
		}
		float moveX = this.move.x * this.speed * this.sprint;
		float moveY = this.move.y * this.speed * this.sprint;
		float moveZ = this.move.z * this.speed * this.sprint;
		this.movePosition(scene, moveX, moveY, moveZ);
	}
}
