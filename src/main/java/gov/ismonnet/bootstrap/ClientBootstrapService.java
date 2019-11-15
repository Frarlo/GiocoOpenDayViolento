package gov.ismonnet.bootstrap;

import java.net.InetSocketAddress;

public interface ClientBootstrapService {

    InetSocketAddress chooseAddress();

    void startWaiting();

    void stopWaiting();
}
