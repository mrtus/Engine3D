package be.mrtus.engine.domain.render.shader;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

public class ShaderProgram {

	private FloatBuffer buffer;
	private int fragmentId;
	private final int programId;
	private final Map<String, Uniform> uniforms = new HashMap();
	private int vertexId;

	public ShaderProgram() throws Exception {
		this.programId = GL20.glCreateProgram();
		if(this.programId == 0) {
			throw new Exception("Could not create Shader");
		}
	}

	public void bind() {
		GL20.glUseProgram(this.programId);
	}

	public void createFragmentShader(String shaderCode) throws Exception {
		this.fragmentId = this.createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}

	public void createUniform(String uniformName) throws Exception {
		int uniformLocation = GL20.glGetUniformLocation(this.programId, uniformName);
		if(uniformLocation < 0) {
			throw new Exception("Could not create uniform: " + uniformName);
		}
		this.uniforms.put(uniformName, new Uniform(uniformLocation, BufferUtils.createFloatBuffer(16)));
	}

	public void createVertexShader(String shaderCode) throws Exception {
		this.vertexId = this.createShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}

	public void destroy() {
		this.unbind();
		if(this.programId != 0) {
			if(this.vertexId != 0) {
				GL20.glDetachShader(this.programId, this.vertexId);
			}
			if(this.fragmentId != 0) {
				GL20.glDetachShader(this.programId, this.fragmentId);
			}
			GL20.glDeleteProgram(this.programId);
		}
	}

	public void link() throws Exception {
		GL20.glLinkProgram(this.programId);
		if(GL20.glGetProgrami(this.programId, GL20.GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + GL20.glGetShaderInfoLog(this.programId, 1024));
		}
		GL20.glValidateProgram(this.programId);
		if(GL20.glGetProgrami(this.programId, GL20.GL_VALIDATE_STATUS) == 0) {
			System.err.println("Warning validating Shader code: " + GL20.glGetShaderInfoLog(this.programId, 1024));
		}
	}

	public void setUniform(String uniformName, Matrix4f matrix) {
		Uniform uniform = this.uniforms.get(uniformName);
		matrix.get(uniform.getBuffer());
		GL20.glUniformMatrix4fv(uniform.getId(), false, uniform.getBuffer());
	}

	public void setUniform(String uniformName, int value) {
		GL20.glUniform1i(this.uniforms.get(uniformName).getId(), value);
	}

	public void unbind() {
		GL20.glUseProgram(0);
	}

	protected int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = GL20.glCreateShader(shaderType);
		if(shaderId == 0) {
			throw new Exception("Error creating shader. Code: " + shaderId);
		}

		GL20.glShaderSource(shaderId, shaderCode);
		GL20.glCompileShader(shaderId);

		if(GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + GL20.glGetShaderInfoLog(shaderId, 1024));
		}

		GL20.glAttachShader(this.programId, shaderId);
		return shaderId;
	}
}
