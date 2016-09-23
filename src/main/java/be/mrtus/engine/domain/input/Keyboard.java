package be.mrtus.engine.domain.input;

import be.mrtus.engine.domain.Display;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard {

	private final Display display;
	private GLFWKeyCallback keyCallback;
	private Map<String, Integer> keyInputs = new HashMap<>();
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

//	public boolean isKeyPressed(int key) {
//		return GLFW.glfwGetKey(this.display.getDisplayId(), key) == GLFW.GLFW_PRESS;
//	}
	public boolean isKeyPressed(String action) {
		return GLFW.glfwGetKey(this.display.getDisplayId(), this.keyInputs.get(action)) == GLFW.GLFW_PRESS;
	}

	public void removeKeyListener(KeyListener listener) {
		this.keyListeners.remove(listener);
	}

	private void onKeyInvoked(int key, int action, int modifier) {
		this.keyListeners.forEach(l -> l.keyPressed(key, action, modifier));
	}

	public void setKey(String action, int key) {
		this.keyInputs.put(action, key);
	}
}
