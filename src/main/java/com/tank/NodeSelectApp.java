package com.tank;

import com.tank.server.TccServer;

/**
 * @author tank198435163.com
 */
public class NodeSelectApp {

  public static void main(final String[] args) {
    TccServer tccServer = new TccServer();
    tccServer.receiveConnection();
  }

}
