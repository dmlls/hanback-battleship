package com.diego.hanbackbattleship.model;

import java.util.HashMap;
import java.util.Map;
import java.lang.ref.WeakReference;


public class DataHolder {
    private Map<String, WeakReference<Object>> data = new HashMap<>();

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}


    public void save(String id, Object object) {
        data.put(id, new WeakReference<Object>(object));
    }

    public Object retrieve(String id) {
        WeakReference<Object> objectWeakReference = data.get(id);
        if (objectWeakReference != null) {
            return objectWeakReference.get();
        }
        return null;
    }
}
