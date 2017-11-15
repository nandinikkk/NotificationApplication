package com.location.reminder.util;

import java.io.Serializable;

import com.google.gson.Gson;

public class GsonUtil<T extends Serializable> {

	protected Class<T> entityClass;

	public GsonUtil(Class<T> entityClass) {

		this.entityClass = entityClass;
	}

	public T jsonObjectDeocde(String json) {
		Gson gson = new Gson();

		return gson.fromJson(json, entityClass);
	}

	public String jsonObjectEncode(T entityClass) {
		Gson gson = new Gson();
		return gson.toJson(entityClass);
	}

}
