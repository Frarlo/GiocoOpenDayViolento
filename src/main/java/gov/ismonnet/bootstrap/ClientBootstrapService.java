package gov.ismonnet.bootstrap;

import gov.ismonnet.bootstrap.swing.UndecidedException;

import java.net.InetSocketAddress;

public interface ClientBootstrapService {

    InetSocketAddress chooseAddress() throws UndecidedException;

    void startWaiting();

    void stopWaiting();
}
