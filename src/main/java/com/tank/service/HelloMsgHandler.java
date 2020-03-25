package com.tank.service;

import com.tank.message.MsgType;
import com.tank.server.TccServer;

import java.net.Socket;

public class HelloMsgHandler implements MessageHandler {

  @Override
  public void process(String messageStr, Socket socket) {
    TccServer.sockets.remove(messageStr);
    TccServer.sockets.putIfAbsent(messageStr, socket);
  }

  @Override
  public int messageType() {
    return MsgType.HELLO.ordinal();
  }
}
