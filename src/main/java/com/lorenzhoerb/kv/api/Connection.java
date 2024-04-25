package com.lorenzhoerb.kv.api;

import java.io.IOException;
import java.net.Socket;

public class Connection {
    public static IKeyValueSession getSession(String host, int port) throws IOException {
        Socket socket = new Socket(host, port);
        return new KeyValueSession(socket);
    }
}
