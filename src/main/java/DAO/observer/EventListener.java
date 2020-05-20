package DAO.observer;

import Model.IdentifierEntity;

public interface EventListener {

	void update(String eventType, IdentifierEntity o);

}
