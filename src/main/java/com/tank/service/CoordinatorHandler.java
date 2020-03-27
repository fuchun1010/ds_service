package com.tank.service;

import com.tank.server.TccServer;

import java.io.BufferedOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Map;

public class CoordinatorHandler implements MessageHandler {

  @Override
  public void process(byte[] data, Socket socket) {
    ByteBuffer buffer = ByteBuffer.allocate(8 + data.length);
    buffer.putInt(1);
    buffer.putInt(data.length);
    buffer.put(data);
    //TODO 这里需要对所有的coordinator群发
    byte[] result = buffer.array();
    try {
      for (Map.Entry<String, Socket> socketEntry : TccServer.sockets.entrySet()) {
        if (!socketEntry.getValue().isConnected()) {
          continue;
        }
        BufferedOutputStream out = new BufferedOutputStream(socketEntry.getValue().getOutputStream());
        out.write(result);
        out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
