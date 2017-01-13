package be.mrtus.engine.domain;

import org.lwjgl.glfw.GLFW;

public class Timer {

	private int fps;
	private int fpsCount;
	private double lastLoopTime;
	private float timeCount;
	private int ups;
	private int upsCount;

	public float getDelta() {
		double time = this.getTime();
		float delta = (float)(time - this.lastLoopTime);
		this.lastLoopTime = time;
		this.timeCount += delta;
		return delta;
	}

	public int getFps() {
		return this.fps;
	}

	public int getUps() {
		return this.ups;
	}

	public double getTime() {
		return GLFW.glfwGetTime();
	}

	public void init() {
		this.lastLoopTime = this.getTime();
	}

	public void update() {
		if(this.timeCount > 1f) {
			this.fps = this.fpsCount;
			this.fpsCount = 0;
			this.ups = this.upsCount;
			this.upsCount = 0;
			this.timeCount = 0;
//			System.out.println("FPS: " + this.fps + " | UPS: " + this.ups);
		}
	}

	public void updateFPS() {
		this.fpsCount++;
	}

	public void updateUPS() {
		this.upsCount++;
	}
}
