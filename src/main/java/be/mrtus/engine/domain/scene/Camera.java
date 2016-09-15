package be.mrtus.engine.domain.scene;

import be.mrtus.engine.domain.input.Keyboard;
import be.mrtus.engine.domain.input.Mouse;
import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.MovableTransform;

public class Camera extends Entity {

	public Camera(Keyboard keyboard, Mouse mouse) {
		super(new Entity.Builder().transform(new MovableTransform.Builder().build()).controller(new CameraController(keyboard, mouse)));
	}

	@Override
	public void update() {
		this.controller.update();
	}

	public void reset() {
		this.setTransform(new MovableTransform.Builder().build());
	}
}
