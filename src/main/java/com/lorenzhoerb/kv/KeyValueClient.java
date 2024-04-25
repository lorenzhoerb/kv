package com.lorenzhoerb.kv;

import com.lorenzhoerb.kv.api.Connection;
import com.lorenzhoerb.kv.api.IKeyValueSession;

import java.io.IOException;

public class KeyValueClient {
    public static void main(String[] args) throws IOException {
        IKeyValueSession session = Connection.getSession("localhost", 6543);
        session.put("name", "KV");
        session.put("version", "1.0.0");

        System.out.println(session.getAll());
        System.out.println(session.get("name"));
        session.clear();

        System.out.println(session.getAll());
        session.put("name", "lorenz");
        session.put("job", "IT");

        System.out.println(session.getAll());
        session.del("name");
        System.out.println(session.getAll());
    }
}
