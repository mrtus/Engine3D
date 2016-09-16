package be.mrtus.engine.domain.scene.entity.light;

import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class PointLight extends Entity {

	private Attenuation attenuation;

	public PointLight(Vector3f position, Vector3f rotation) {
		super(new Entity.Builder()
				//				.model(new PointLightModel())
				.transform(new Transform.Builder()
						.position(position)
						.rotation(rotation)
						.build()));
	}

	public Attenuation getAttenuation() {
		return this.attenuation;
	}

	public Matrix4f getColor() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Matrix4f getIntensity() {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public class Attenuation {

		public Matrix4f getConstant() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public Matrix4f getExponent() {
			throw new UnsupportedOperationException("Not supported yet.");
		}

		public Matrix4f getLinear() {
			throw new UnsupportedOperationException("Not supported yet.");
		}
	}
}
