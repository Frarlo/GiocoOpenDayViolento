package gov.ismonnet.bootstrap;

import gov.ismonnet.lifecycle.LifeCycleService;

public interface ServerBootstrapService {

    int choosePort() throws UndecidedException;

    void startWaiting(LifeCycleService lifeCycleService);

    void stopWaiting();
}
