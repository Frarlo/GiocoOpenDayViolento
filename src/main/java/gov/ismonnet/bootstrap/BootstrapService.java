package gov.ismonnet.bootstrap;

public interface BootstrapService {

    NetSide chooseNetSide() throws UndecidedException;

    enum NetSide { CLIENT, SERVER }
}
