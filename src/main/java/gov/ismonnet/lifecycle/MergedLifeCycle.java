package gov.ismonnet.lifecycle;

import gov.ismonnet.util.SneakyThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class MergedLifeCycle implements LifeCycleService {

    private final List<LifeCycleService> lifeCycleServices;

    MergedLifeCycle(LifeCycleService... services) {
        this(Arrays.asList(services));
    }

    MergedLifeCycle(List<LifeCycleService> lifeCycleServices) {
        if(lifeCycleServices.size() < 2)
            throw new AssertionError("Cannot merge less than 2 lifecycles");
        this.lifeCycleServices = new ArrayList<>(lifeCycleServices);
    }

    @Override
    public void start() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void stop()  {
        System.out.println("Stopping merged lifecycle " + this);
        // By stopping the last one, the afterStop hooks are going to stop all of them
        SneakyThrow.runUnchecked(() -> lifeCycleServices.get(lifeCycleServices.size() - 1).stop());
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

        // The first being stopped is actually the last in the list
        final LifeCycleService last = lifeCycleServices.get(lifeCycleServices.size() - 1);
        last.beforeStop(() -> onStop.accept(other));
        other.afterStop(() -> onStop.accept(this));

        return new MergedLifeCycle(this, other);
    }

    @Override
    public void beforeStop(Runnable runnable) {
        lifeCycleServices.get(lifeCycleServices.size() - 1).beforeStop(runnable);
    }

    @Override
    public void afterStop(Runnable runnable) {
        lifeCycleServices.get(0).afterStop(runnable);
    }

    @Override
    public void register(LifeCycle lifeCycle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unregister(LifeCycle lifeCycle) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "MergedLifeCycle{" +
                "lifeCycleServices=" + lifeCycleServices +
                "} ";
    }
}
