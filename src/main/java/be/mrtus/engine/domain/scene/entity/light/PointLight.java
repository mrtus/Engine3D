package be.mrtus.engine.domain.scene.entity.light;

import be.mrtus.engine.domain.scene.entity.Entity;
import be.mrtus.engine.domain.scene.entity.component.Transform;
import org.joml.Vector3f;

public class PointLight extends Entity {

	private Attenuation attenuation;
	private Vector3f color;
	private float intensity;

	public PointLight(Vector3f position, Vector3f rotation, float intensity) {
		super(new Entity.Builder()
				//				.model(new PointLightModel())
				.transform(new Transform.Builder()
						.position(position)
						.rotation(rotation)
						.build()));
		this.intensity = intensity;
		this.attenuation = new Attenuation(0, 0, 1);
	}

	public Attenuation getAttenuation() {
		return this.attenuation;
	}

	public void setAttenuation(Attenuation att) {
		this.attenuation = att;
	}

	public Vector3f getColor() {
		return this.color;
	}

	public float getIntensity() {
		return this.intensity;
	}

	public class Attenuation {

		private float constant;
		private float linear;
		private float exponent;

		public Attenuation(float constant, float linear, float exponent) {
			this.constant = constant;
			this.linear = linear;
			this.exponent = exponent;
		}

		public float getConstant() {
			return this.constant;
		}

		public void setConstant(float constant) {
			this.constant = constant;
		}

		public float getLinear() {
			return this.linear;
		}

		public void setLinear(float linear) {
			this.linear = linear;
		}

		public float getExponent() {
			return this.exponent;
		}

		public void setExponent(float exponent) {
			this.exponent = exponent;
		}
	}
}
