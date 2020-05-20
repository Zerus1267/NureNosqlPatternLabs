package DAO.observer.listeners;

import DAO.observer.EventListener;
import Model.IdentifierEntity;

public class EntityReadListener implements EventListener {

	private String tableName;

	public EntityReadListener() {
	}

	public EntityReadListener(String tableName) {
		this.tableName = tableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public void update(String eventType, IdentifierEntity o) {
		System.out.println("Reading entity from " + tableName + " with id = " + o.getId());
	}
}
