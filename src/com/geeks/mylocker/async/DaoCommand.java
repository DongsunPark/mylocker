package com.geeks.mylocker.async;

import de.greenrobot.dao.AbstractDao;

public class DaoCommand<T> {

	public static enum CRUD {
			INSERT, UPDATE, DELETE, SELECT
	}
	
	private AbstractDao<T, Long> dao;
	
	private T entity;
	
	private CRUD crud;

	public DaoCommand(AbstractDao<T, Long> dao, T entity, CRUD crud) {
		super();
		this.dao = dao;
		this.entity = entity;
		this.crud = crud;
	}

	public AbstractDao<T, Long> getDao() {
		return dao;
	}

	public T getEntity() {
		return entity;
	}

	public CRUD getCrud() {
		return crud;
	}
	
	
}
