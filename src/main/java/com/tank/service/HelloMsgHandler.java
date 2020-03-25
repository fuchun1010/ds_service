package com.tank.service;

import com.tank.server.TccServer;

import java.net.Socket;
import java.util.logging.Logger;

public class HelloMsgHandler implements MessageHandler {

  @Override
  public void process(byte[] message, Socket socket) {
    String appName = new String(message);
    TccServer.addSocket(appName, socket);
    logger.info(String.format("client :[%s] connect success, online is:[%d]", appName, TccServer.online()));
  }


  private Logger logger = Logger.getLogger(HelloMsgHandler.class.getSimpleName());
}
