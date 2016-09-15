package be.mrtus.gameengine.domain.input;

import be.mrtus.gameengine.domain.Display;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard {

	private final Display display;
	private GLFWKeyCallback keyCallback;
	private final List<KeyListener> keyListeners = new ArrayList<>();

	public Keyboard(Display display) {
		this.display = display;
	}

	public void addKeyListener(KeyListener listener) {
		this.keyListeners.add(listener);
	}

	public void destroy() {
		this.keyCallback.release();
	}

	public void init() {
		GLFW.glfwSetKeyCallback(this.display.getDisplayId(), this.keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				onKeyInvoked(key, action, mods);
			}
		});
	}

	public boolean isKeyPressed(int key) {
		return GLFW.glfwGetKey(this.display.getDisplayId(), key) == GLFW.GLFW_PRESS;
	}

	public void removeKeyListener(KeyListener listener) {
		this.keyListeners.remove(listener);
	}

	private void onKeyInvoked(int key, int action, int modifier) {
		this.keyListeners.forEach(l -> l.keyPressed(key, action, modifier));
	}
}
