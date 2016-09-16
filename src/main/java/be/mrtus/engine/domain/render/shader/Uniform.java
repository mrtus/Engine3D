package be.mrtus.engine.domain.render.shader;

import java.nio.FloatBuffer;

public class Uniform {

	private final int id;
	private FloatBuffer buffer;

	public Uniform(int id, FloatBuffer buffer) {
		this.id = id;
		this.buffer = buffer;
	}

	public FloatBuffer getBuffer() {
		return this.buffer;
	}

	public int getId() {
		return this.id;
	}
}
