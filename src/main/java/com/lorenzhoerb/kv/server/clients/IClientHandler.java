package com.lorenzhoerb.kv.server.clients;

import java.io.InputStream;
import java.io.OutputStream;

public interface IClientHandler extends Runnable {
    void handle(InputStream in, OutputStream out);
}
