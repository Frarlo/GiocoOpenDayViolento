package gov.ismonnet.bootstrap;

public interface ServerBootstrapService {

    int choosePort();

    void startWaiting();

    void stopWaiting();
}
