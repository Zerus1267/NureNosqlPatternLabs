package DAO.observer.listeners;

import DAO.observer.EventListener;
import Model.IdentifierEntity;

public class EntityWriteListener implements EventListener {

	private String tableName;

	public EntityWriteListener(String tableName){
		this.tableName = tableName;
	}

	@Override
	public void update(String eventType, IdentifierEntity o) {
		System.out.println("Writing new entity to" + tableName + "with name = " + o.getEntityName());
	}
}
