package be.mrtus.engine.domain.input;

import be.mrtus.engine.domain.Display;
import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;

public class Mouse {

	private final Vector2d currentPos;
	private GLFWCursorEnterCallback cursorEnterCallback;
	private GLFWCursorPosCallback cursorPostCallback;
	private final Vector2d deltaPos;
	private final Display display;
	private boolean inWindow = false;
	private final Vector2d previousPos;

	public Mouse(Display display) {
		this.display = display;
		this.previousPos = new Vector2d(-1, -1);
		this.currentPos = new Vector2d(0, 0);
		this.deltaPos = new Vector2d(0, 0);
	}

	public void destroy() {
		this.cursorPostCallback.release();
		this.cursorEnterCallback.release();
	}

	public void init() {
		GLFW.glfwSetCursorPosCallback(this.display.getDisplayId(), this.cursorPostCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				onMouseMoved(xpos, ypos);
			}
		});
		GLFW.glfwSetCursorEnterCallback(this.display.getDisplayId(), this.cursorEnterCallback = new GLFWCursorEnterCallback() {
			@Override
			public void invoke(long window, int entered) {
				onMouseEnteredWindow(entered);
			}
		});
	}

	public boolean isLeftButtonPressed() {
		return this.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_LEFT);
	}

	public boolean isMiddleButtonPressed() {
		return this.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_MIDDLE);
	}

	public boolean isMouseButtonPressed(int button) {
		return GLFW.glfwGetMouseButton(this.display.getDisplayId(), button) == GLFW.GLFW_PRESS;
	}

	public boolean isRightButtonPressed() {
		return this.isMouseButtonPressed(GLFW.GLFW_MOUSE_BUTTON_RIGHT);
	}

	public void update() {
		if(this.previousPos.x > 0 && this.previousPos.y > 0 && this.inWindow) {
			this.deltaPos.x = this.currentPos.x - this.previousPos.x;
			this.deltaPos.y = this.currentPos.y - this.previousPos.y;
		}
		this.previousPos.x = this.currentPos.x;
		this.previousPos.y = this.currentPos.y;
	}

	private void onMouseEnteredWindow(int entered) {
		this.inWindow = (entered == 1);
	}

	private void onMouseMoved(double xpos, double ypos) {
		this.currentPos.x = xpos;
		this.currentPos.y = ypos;
	}
}
