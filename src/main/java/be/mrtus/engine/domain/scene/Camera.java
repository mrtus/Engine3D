package be.mrtus.engine.domain.scene;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.MovableTransform;
import org.joml.Vector3f;

public class Camera extends Entity {

	public Camera(Keyboard keyboard, Mouse mouse, Scene scene) {
		super(new Entity.Builder()
				.controller(new CameraController(keyboard, mouse, scene))
		//				.model(new Model.ModelBuilder()
		//						.setPositions(new float[]{-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f}, new int[]{0, 1, 3, 3, 1, 2, 8, 10, 11, 9, 8, 11, 12, 13, 7, 5, 12, 7, 6, 15, 14, 6, 14, 4, 19, 18, 16, 19, 16, 17, 7, 6, 4, 7, 4, 5})
		//						.setTexture(new Texture("/grassblock.png"), new float[]{0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 0.0f, 0.5f, 0.0f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.5f, 0.5f, 0.0f, 0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.5f, 0.5f, 1.0f, 0.5f})
		//						.build())
		);
		this.reset();
	}

	public void reset() {
		this.setTransform(new MovableTransform.Builder()
				.rotation(new Vector3f(0f, 180f, 0f))
				//						.scale(new Vector3f(0.1f, 0.1f, 0.1f))
				.build());
	}

	@Override
	public void update() {
		this.controller.update();
	}
}
