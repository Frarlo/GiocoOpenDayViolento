package gov.ismonnet.lifecycle;

import gov.ismonnet.util.SneakyThrow;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class LifeCycleManager implements LifeCycleService {

    private final List<LifeCycle> registered;

    private final AtomicBoolean started;
    private int startedServices;

    @Inject public LifeCycleManager() {
        registered = new CopyOnWriteArrayList<>();
        started = new AtomicBoolean(false);
    }

    @Override
    public void start() {
        if(started.getAndSet(true))
            throw new AssertionError("LifeCycle already started");

        System.out.println("Starting lifecycle");
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

        System.out.println("Stopping lifecycle");
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
    }

    @Override
    public void register(LifeCycle lifeCycle) {
        registered.add(lifeCycle);
    }

    @Override
    public void unregister(LifeCycle lifeCycle) {
        registered.remove(lifeCycle);
    }
}
