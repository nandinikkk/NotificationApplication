package com.location.reminder.util;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

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


    public List<T> jsonArrayDecode(JSONArray jsonArray) {

        List<T> lst = new ArrayList<T>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                T entity = jsonObjectDeocde(jsonArray.getJSONObject(i).toString());
                lst.add(entity);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

//        TypeToken<List<T>> type = new TypeToken<List<T>>() {
//        };
//        List<T> list = new Gson().fromJson(json, type.getType());
        return lst;
    }


}
