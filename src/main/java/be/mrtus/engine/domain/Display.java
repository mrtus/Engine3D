package be.mrtus.engine.domain;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Display {

	private final List<DisplayListener> displayListeners = new ArrayList<>();
	private GLFWErrorCallback errorCallback;
	private boolean fullscreen;
	private int height;
	private long id;
	private boolean resized;
	private final String title;
	private boolean vsync;
	private int width;
	private GLFWWindowSizeCallback windowSizeCallback;

	public Display(String title, int width, int height, boolean fullscreen, boolean vsync) {
		this.title = title;
		this.width = width;
		this.height = height;
		this.fullscreen = fullscreen;
		this.vsync = vsync;
	}

	public void closeWindow() {
		GLFW.glfwSetWindowShouldClose(this.id, GL11.GL_TRUE);
	}

	public void destroy() {
		this.windowSizeCallback.release();
		this.errorCallback.release();
		GLFW.glfwDestroyWindow(this.id);
	}

	public long getDisplayId() {
		return this.id;
	}

	public void setFullscreen(boolean fullscreen) {
		if(this.fullscreen != fullscreen) {
			this.setResized(true);
			this.fullscreen = fullscreen;
			this.destroy();
			this.init();
		}
	}

	public int getHeight() {
		return this.height;
	}

	public int getWidth() {
		return this.width;
	}

	public void init() {
		GLFW.glfwSetErrorCallback(this.errorCallback = Callbacks.errorCallbackPrint(System.err));
		if(GLFW.glfwInit() != GL11.GL_TRUE) {
			throw new IllegalStateException("Cannot initialized GLFW");
		}

		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);

		ByteBuffer vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
//		ByteBuffer vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetMonitors().get(2));
		if(this.fullscreen) {
			this.id = GLFW.glfwCreateWindow(GLFWvidmode.width(vidMode), GLFWvidmode.height(vidMode), this.title, GLFW.glfwGetPrimaryMonitor(), 0);
		} else {
			this.id = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);
		}
		if(this.id == 0) {
			GLFW.glfwTerminate();
			throw new RuntimeException("Failed to create the GLFW window.");
		}

		GLFW.glfwSetWindowSizeCallback(this.id, this.windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int w, int h) {
				Display.this.width = w;
				Display.this.height = h;
				Display.this.setResized(true);
			}
		});

		if(!this.fullscreen) {
			GLFW.glfwSetWindowPos(this.id, (GLFWvidmode.width(vidMode) - this.width) / 2, (GLFWvidmode.height(vidMode) - this.height) / 2);
		}

		GLFW.glfwMakeContextCurrent(this.id);

		GLContext.createFromCurrent();
		GL.createCapabilities(true);

		this.setVSync(this.vsync);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);

		GLFW.glfwShowWindow(this.id);
		System.out.println("Display ID " + this.id);
	}

	public boolean isClosing() {
		return GLFW.glfwWindowShouldClose(this.id) == GL11.GL_TRUE;
	}

	public boolean isResized() {
		return this.resized;
	}

	public void setResized(boolean resized) {
		if(this.resized != resized) {
			this.displayListeners.forEach(l -> l.onDisplayResize(this));
		}
		this.resized = resized;
	}

	public boolean isVSync() {
		return this.vsync;
	}

	public void setVSync(boolean vsync) {
		this.vsync = vsync;
		GLFW.glfwSwapInterval(this.vsync ? 1 : 0);
	}

	public void removeListener(DisplayListener listener) {
		this.displayListeners.remove(listener);
	}

	public void toggleFullscreen() {
		this.setFullscreen(!this.fullscreen);
	}

	public void update() {
		GLFW.glfwSwapBuffers(this.id);
		GLFW.glfwPollEvents();
	}
}
