package gov.ismonnet.lifecycle;

public interface LifeCycleService {

    void start();

    void stop();

    void register(LifeCycle lifeCycle);

    void unregister(LifeCycle lifeCycle);
}
