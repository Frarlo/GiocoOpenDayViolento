package gov.ismonnet.lifecycle;

public interface LifeCycleService extends LifeCycle {

    void register(LifeCycle lifeCycle);

    void unregister(LifeCycle lifeCycle);

    LifeCycleService merge(LifeCycleService lifeCycleService);

    void beforeStop(Runnable runnable);

    void afterStop(Runnable runnable);
}
