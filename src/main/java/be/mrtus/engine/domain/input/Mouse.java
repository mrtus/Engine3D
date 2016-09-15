package be.mrtus.engine.domain.input;

import be.mrtus.engine.domain.Display;
import java.nio.DoubleBuffer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.opengl.GL11;

public class Mouse {

	private final Vector2f currentPos;
	private GLFWCursorEnterCallback cursorEnterCallback;
	private GLFWCursorPosCallback cursorPostCallback;
	private final Vector2f deltaPos;
	private final Display display;
	private boolean inWindow = false;
	private boolean mouseGrabbed;
	private final Vector2f previousPos;

	public Mouse(Display display) {
		this.display = display;
		this.previousPos = new Vector2f(-1f, -1f);
		this.currentPos = new Vector2f(0, 0);
		this.deltaPos = new Vector2f(0, 0);
	}

	public void destroy() {
		this.cursorPostCallback.release();
		this.cursorEnterCallback.release();
	}

	public Vector2f getDeltaPos() {
		return this.deltaPos;
	}

	public void init() {
		GLFW.glfwSetCursorPosCallback(this.display.getDisplayId(), this.cursorPostCallback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				onMouseMoved((float)xpos, (float)ypos);
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
		this.deltaPos.x = 0;
		this.deltaPos.y = 0;

		if(this.isLeftButtonPressed()) {
			this.setMouseGrabbed(true);
			GLFW.glfwSetCursorPos(this.display.getDisplayId(), this.display.getWidth() / 2, this.display.getHeight() / 2);
		}
		if(this.mouseGrabbed) {
			DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
			DoubleBuffer y = BufferUtils.createDoubleBuffer(1);

			GLFW.glfwGetCursorPos(this.display.getDisplayId(), x, y);
			x.rewind();
			y.rewind();

			this.currentPos.x = (float)x.get();
			this.currentPos.y = (float)y.get();
			this.deltaPos.x = this.currentPos.x - this.display.getWidth() / 2;
			this.deltaPos.y = this.currentPos.y - this.display.getHeight() / 2;
			GLFW.glfwSetCursorPos(this.display.getDisplayId(), this.display.getWidth() / 2, this.display.getHeight() / 2);
		} else {
			;
		}
	}

	private boolean isMouseGrabbed() {
		return this.mouseGrabbed;
	}

	public void setMouseGrabbed(boolean b) {
		this.mouseGrabbed = b;
		if(b) {
			GLFW.glfwSetInputMode(this.display.getDisplayId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
		} else {
			GLFW.glfwSetInputMode(this.display.getDisplayId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
		}
	}

	private void onMouseEnteredWindow(int entered) {
		this.inWindow = (entered == GL11.GL_TRUE);
	}

	private void onMouseMoved(float xpos, float ypos) {
//		this.currentPos.x = xpos;
//		this.currentPos.y = ypos;
	}
}
