package com.tank.service;

import java.net.Socket;

public interface MessageHandler {

  void process(String messageStr, Socket socket);

  int messageType();
}
