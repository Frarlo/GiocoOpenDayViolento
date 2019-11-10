package gov.ismonnet.bootstrap;

import java.net.InetSocketAddress;

public interface BootstrapService {

    NetSide chooseNetSide();

    int choosePort();

    InetSocketAddress chooseAddress();

    enum NetSide { CLIENT, SERVER }
}
