package com.tank.service;

import com.tank.server.TccServer;

import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.logging.Logger;

public class HelloMsgHandler implements MessageHandler {

  @Override
  public void process(byte[] message, Socket socket) {
    String appName = new String(message);
    TccServer.addSocket(appName, socket);
    for (Map.Entry<String, Socket> socketEntry : TccServer.sockets.entrySet()) {
      Socket target = socketEntry.getValue();
      if (target.isClosed()) {
        continue;
      }
      String targetName = socketEntry.getKey();
      String content = String.format("hi,%s, %s conencted", targetName, appName);
      byte[] data = content.getBytes();
      ByteBuffer buffer = ByteBuffer.allocate(8 + data.length);
      buffer.putInt(0);
      buffer.putInt(data.length);
      buffer.put(data);
      try {
        BufferedOutputStream out = new BufferedOutputStream(target.getOutputStream());
        out.write(buffer.array());
        out.flush();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    logger.info(String.format("client :[%s] connect success, online is:[%d]", appName, TccServer.online()));
  }


  private Logger logger = Logger.getLogger(HelloMsgHandler.class.getSimpleName());
}
