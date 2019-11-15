package gov.ismonnet.bootstrap;

public interface BootstrapService {

    NetSide chooseNetSide();

    enum NetSide { CLIENT, SERVER }
}
