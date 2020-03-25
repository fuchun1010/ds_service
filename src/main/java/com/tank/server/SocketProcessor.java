package com.tank.server;

import com.tank.service.MessageHandler;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class SocketProcessor implements Runnable {

  public SocketProcessor(final Socket socket) {
    this.socket = socket;
  }

  @Override
  public void run() {
    if (this.socket == null) {
      return;
    }

    try {
      InputStream in = new BufferedInputStream(this.socket.getInputStream());
      byte[] data = new byte[1024];
      for (; ; ) {
        int result = in.read(data);
        if (result == -1) {
          Thread.sleep(200);
          continue;
        }
        //TODO 考虑因为网络产生的粘包
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int type = buffer.getInt();
        int bodyLen = buffer.getInt();
        byte[] body = new byte[bodyLen];
        if (bodyLen <= buffer.remaining()) {
          ByteBuffer xx = buffer.get(body);
          xx.flip();
          xx.clear();
          MessageHandler messageHandler = TccServer.handlers.get(type);
          if (messageHandler != null) {
            messageHandler.process(body, socket);
          }

        }
      }
    } catch (Exception e) {
      logger.warning("解析异常:" + e.getMessage());
    }

  }

  private Socket socket;

  private Logger logger = Logger.getLogger(SocketProcessor.class.getSimpleName());
}
