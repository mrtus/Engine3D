package be.mrtus.engine.domain.scene;

import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.component.EntityController;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class CameraController extends EntityController<Camera> {

	private final Keyboard keyboard;
	private final Mouse mouse;
	private final Vector3f move = new Vector3f();

	public CameraController(Keyboard keyboard, Mouse mouse) {
		this.keyboard = keyboard;
		this.mouse = mouse;
	}

	public void movePosition(float offsetX, float offsetY, float offsetZ) {
		Vector3f position = this.entity.getPosition();
		Vector3f rotation = this.entity.getTransform().getRotation();
		if(offsetZ != 0) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y)) * -offsetZ;
			position.z += (float)Math.cos(Math.toRadians(rotation.y)) * offsetZ;
		}
		if(offsetX != 0) {
			position.x += (float)Math.sin(Math.toRadians(rotation.y - 90)) * -offsetX;
			position.z += (float)Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
		}
		position.y += offsetY;
	}

	public void moveRotation(float offsetX, float offsetY, float offsetZ) {
		Vector3f rotation = this.entity.getTransform().getRotation();
		rotation.y += offsetX * 0.1;
		rotation.x += offsetY * 0.1;
		rotation.z += offsetZ * 0.1;
	}

	@Override
	public void update() {
		Vector2f deltaPos = this.mouse.getDeltaPos();
		this.moveRotation(deltaPos.x, deltaPos.y, 0);

		this.move.set(0, 0, 0);
		if(this.keyboard.isKeyPressed(GLFW.GLFW_KEY_W)) {
			this.move.z = -1;
		} else if(this.keyboard.isKeyPressed(GLFW.GLFW_KEY_S)) {
			this.move.z = 1;
		}
		if(this.keyboard.isKeyPressed(GLFW.GLFW_KEY_A)) {
			this.move.x = -1;
		} else if(this.keyboard.isKeyPressed(GLFW.GLFW_KEY_D)) {
			this.move.x = 1;
		}
		if(this.keyboard.isKeyPressed(GLFW.GLFW_KEY_LEFT_CONTROL)) {
			this.move.y = -1;
		} else if(this.keyboard.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
			this.move.y = 1;
		}
		this.movePosition(this.move.x * 0.1f, this.move.y * 0.1f, this.move.z * 0.1f);
	}
}
