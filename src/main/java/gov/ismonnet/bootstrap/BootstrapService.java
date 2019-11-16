package gov.ismonnet.bootstrap;

import gov.ismonnet.bootstrap.swing.UndecidedException;

public interface BootstrapService {

    NetSide chooseNetSide() throws UndecidedException;

    enum NetSide { CLIENT, SERVER }
}
