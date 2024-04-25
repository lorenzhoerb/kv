package com.lorenzhoerb.kv.api;

import java.util.HashMap;

public interface IKeyValueSession {
    void put(String key, String value);
    HashMap<String, String> getAll();
    String get(String key);
    void clear();
    void del(String key);
}
