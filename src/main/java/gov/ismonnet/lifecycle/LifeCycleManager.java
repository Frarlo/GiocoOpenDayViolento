package gov.ismonnet.lifecycle;

import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class LifeCycleManager implements LifeCycleService {

    private final String name;
    private final List<LifeCycle> registered;

    private final AtomicBoolean started;
    private int startedServices;

    protected final List<Runnable> beforeStopListener;
    protected final List<Runnable> afterStopListener;

    @Inject public LifeCycleManager(String name) {
        this.name = name;

        registered = new CopyOnWriteArrayList<>();
        started = new AtomicBoolean(false);

        beforeStopListener = new CopyOnWriteArrayList<>();
        afterStopListener = new CopyOnWriteArrayList<>();
    }

    @Override
    public void start() {
        if(started.getAndSet(true))
            throw new AssertionError("LifeCycle already started");

        System.out.println("Starting lifecycle " + this);
        registered.forEach(lifeCycle -> {
            System.out.println("Starting lifecycle of " + lifeCycle.getClass().getSimpleName());
            startedServices++;
            SneakyThrow.runUnchecked(lifeCycle::start);
        });
    }

    @Override
    public void stop() {
        if(!started.get())
            throw new AssertionError("LifeCycle hasn't been started");

        beforeStopListener.forEach(Runnable::run);

        System.out.println("Stopping lifecycle " + this);
        IntStream.range(0, startedServices)
                .map(i -> startedServices - 1 - i)
                .forEach(i -> {
                    final LifeCycle lifeCycle = registered.get(i);
                    try {
                        System.out.println("Stopping lifecycle of " + lifeCycle.getClass().getSimpleName());
                        lifeCycle.stop();
                    } catch (Throwable t) {
                        System.err.println("Exception while stopping lifecycle of " + lifeCycle.getClass().getSimpleName());
                        t.printStackTrace(System.err);
                    }
                });

        afterStopListener.forEach(Runnable::run);
    }

    @Override
    public void register(LifeCycle lifeCycle) {
        registered.add(lifeCycle);
    }

    @Override
    public void unregister(LifeCycle lifeCycle) {
        registered.remove(lifeCycle);
    }

    @Override
    public LifeCycleService merge(LifeCycleService other) {

        // Register both lifecycle to depend on the other one
        // so if one gets stopped the other does too
        final AtomicBoolean stopping = new AtomicBoolean(false);
        final Consumer<LifeCycleService> onStop = (o) -> {
            if(!stopping.getAndSet(true))
                SneakyThrow.runUnchecked(o::stop);
        };

        beforeStop(() -> onStop.accept(other));
        other.afterStop(() -> onStop.accept(this));

        return new MergedLifeCycle(this, other);
    }

    @Override
    public void beforeStop(Runnable runnable) {
        beforeStopListener.add(runnable);
    }

    @Override
    public void afterStop(Runnable runnable) {
        afterStopListener.add(runnable);
    }

    @Override
    public String toString() {
        return "LifeCycleManager{" +
                "name='" + name + '\'' +
                ", started=" + started +
                ", startedServices=" + startedServices +
                ", beforeStopListener=" + beforeStopListener +
                ", afterStopListener=" + afterStopListener +
                '}';
    }
}
