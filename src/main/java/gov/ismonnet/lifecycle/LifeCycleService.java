package gov.ismonnet.lifecycle;

public interface LifeCycleService extends LifeCycle {

    void register(LifeCycle lifeCycle);

    void unregister(LifeCycle lifeCycle);

    void beforeStop(Runnable runnable);

    void afterStop(Runnable runnable);
}
