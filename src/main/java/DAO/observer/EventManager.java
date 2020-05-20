package DAO.observer;

import Model.IdentifierEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {

	Map<String, List<EventListener>> listeners = new HashMap<>();

	public EventManager(String... operations){
		for(String operation : operations){
			this.listeners.put(operation, new ArrayList<>());
		}
	}

	public void subscribe(String eventType, EventListener listener) {
		unsubscribe(eventType, listener);
		List<EventListener> users = listeners.get(eventType);
		users.add(listener);
	}

	public void unsubscribe(String eventType, EventListener listener) {
		//TODO -> check for null
		List<EventListener> users = listeners.get(eventType);
		if (users.size() != 0) {
			users.remove(listener);
		}
	}

	public void notify(String eventType, IdentifierEntity o) {
		List<EventListener> users = listeners.get(eventType);
		for (EventListener listener : users) {
			listener.update(eventType, o);
		}
	}

	public Map<String, List<EventListener>> getListeners() {
		return listeners;
	}

	public void setListeners(Map<String, List<EventListener>> listeners) {
		this.listeners = listeners;
	}
}
