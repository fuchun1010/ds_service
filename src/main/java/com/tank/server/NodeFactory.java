package com.tank.server;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author tank198435163.com
 */
public class NodeFactory implements ThreadFactory {

  public NodeFactory(final String prefix) {
    this.prefix = prefix;
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r);
    thread.setName(String.format("node-thread-%d", counter.incrementAndGet()));
    return thread;
  }


  private String prefix;


  private AtomicInteger counter = new AtomicInteger(0);
}
