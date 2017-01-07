package be.mrtus.engine.domain.scene;

import be.mrtus.engine.demo.domain.Scene;
import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.MovableTransform;

public class Camera extends Entity {

	public Camera(Keyboard keyboard, Mouse mouse) {
		super(new Entity.Builder().transform(new MovableTransform.Builder().build()).controller(new CameraController(keyboard, mouse)));
	}

	public void reset() {
		this.setTransform(new MovableTransform.Builder().build());
	}

	@Override
	public void update(Scene scene) {
		this.controller.update(scene);
	}
}
