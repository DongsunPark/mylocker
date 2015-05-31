package com.geeks.mylocker.async;

import de.greenrobot.dao.AbstractDao;

public class DaoCommand<Entity> {

	public static enum CRUD {
			INSERT, UPDATE, DELETE, SELECT
	}
	
	private AbstractDao<Entity, Long> dao;
	
	private Entity entity;
	
	private CRUD crud;

	public DaoCommand(AbstractDao<Entity, Long> dao, Entity entity, CRUD crud) {
		super();
		this.dao = dao;
		this.entity = entity;
		this.crud = crud;
	}

	public AbstractDao<Entity, Long> getDao() {
		return dao;
	}

	public Entity getEntity() {
		return entity;
	}

	public CRUD getCrud() {
		return crud;
	}
	
	
}
