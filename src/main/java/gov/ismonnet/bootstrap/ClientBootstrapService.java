package gov.ismonnet.bootstrap;

import gov.ismonnet.lifecycle.LifeCycleService;

import java.net.InetSocketAddress;

public interface ClientBootstrapService {

    InetSocketAddress chooseAddress() throws UndecidedException;

    void startWaiting(LifeCycleService lifeCycleService);

    void stopWaiting();
}
