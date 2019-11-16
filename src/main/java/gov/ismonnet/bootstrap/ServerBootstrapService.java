package gov.ismonnet.bootstrap;

import gov.ismonnet.bootstrap.swing.UndecidedException;

public interface ServerBootstrapService {

    int choosePort() throws UndecidedException;

    void startWaiting();

    void stopWaiting();
}
