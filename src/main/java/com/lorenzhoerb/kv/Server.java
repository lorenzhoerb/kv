package com.lorenzhoerb.kv;

import com.lorenzhoerb.kv.server.KeyValueServer;

public class Server {
    public static void main(String[] args) {
        new KeyValueServer(6543).start();
    }
}
