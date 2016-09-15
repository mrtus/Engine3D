package be.mrtus.engine.domain.scene.entity.component;

import be.mrtus.engine.domain.scene.Controller;
import be.mrtus.engine.domain.scene.entity.Entity;

public class EntityController<E extends Entity> extends Controller {

	protected E entity;

	public void setEntity(E entity) {
		this.entity = entity;
	}
}
