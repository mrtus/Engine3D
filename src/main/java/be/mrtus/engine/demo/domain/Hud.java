package be.mrtus.engine.demo.domain;

import be.mrtus.engine.domain.Display;
import be.mrtus.engine.domain.Timer;
import java.util.ArrayList;
import java.util.List;

public class Hud {

	private final int COL = 16;
	private final int ROW = 16;
	private final String fontFile = "/font.png";
	private final Display display;
	private List<TextEntity> entities = new ArrayList<>();
	private final Scene scene;
	private final Timer timer;

	public Hud(Display display, Timer timer, Scene scene) {
		this.display = display;
		this.scene = scene;
		this.timer = timer;
		TextEntity test = new TextEntity("Test", this.fontFile, COL, ROW);
		test.setPosition(0, 0);
		this.entities.add(test);
	}

	public List<TextEntity> getEntities() {
		return this.entities;
	}

	public void update() {

	}

	public void destroy() {

	}
}
