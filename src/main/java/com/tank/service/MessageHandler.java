package com.tank.service;

import java.net.Socket;

public interface MessageHandler {

  void process(byte[] message, Socket socket);
}
