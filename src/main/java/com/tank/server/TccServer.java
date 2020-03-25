package com.tank.server;

import com.google.common.collect.Maps;
import com.tank.message.MsgType;
import com.tank.service.HelloMsgHandler;
import com.tank.service.MessageHandler;
import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class TccServer {


  public TccServer() {
    this.port = 10001;
    this.minSize = 1;
    this.maxSize = 50;
  }

  public TccServer(int port) {
    this.port = port;
  }

  public TccServer(int port, int minSize, int maxSize) {
    this(port);
    this.minSize = minSize;
    this.maxSize = maxSize;
  }

  public TccServer(int minSize, int maxSize) {
    this();
    this.minSize = minSize;
    this.maxSize = maxSize;
  }


  @SneakyThrows
  public void receiveConnection() {
    ThreadPoolExecutor threadPoolExecutor = this.init();
    ServerSocket serverSocket = new ServerSocket(this.port);
    this.registMessageHandler();
    logger.info(String.format("server listening on port:[%d]", this.port));
    for (; ; ) {
      Socket socket = serverSocket.accept();
      threadPoolExecutor.submit(new SocketProcessor(socket));
    }

  }

  private ThreadPoolExecutor init() {
    ThreadPoolExecutor executor = new ThreadPoolExecutor(
        this.minSize,
        this.maxSize >= 100 ? 100 : this.maxSize,
        500,
        TimeUnit.MILLISECONDS,
        new ArrayBlockingQueue<>(100),
        new NodeFactory("socket-processor")
    );

    return executor;
  }

  public static void addSocket(String appName, Socket socket) {
    sockets.remove(appName);
    sockets.putIfAbsent(appName, socket);
  }

  public static int online() {
    return sockets.size();
  }

  private Map<Integer, MessageHandler> registMessageHandler() {
    handlers.putIfAbsent(MsgType.HELLO.ordinal(), new HelloMsgHandler());
    return handlers;
  }


  private int port;

  private int minSize;

  private int maxSize;

  private Logger logger = Logger.getLogger(TccServer.class.getSimpleName());

  public static Map<Integer, MessageHandler> handlers = Maps.newHashMap();

  public static ConcurrentHashMap<String, Socket> sockets = new ConcurrentHashMap<>();


}
