package gov.ismonnet.netty.core;

import gov.ismonnet.event.EventBus;
import gov.ismonnet.event.EventListener;

import java.util.concurrent.CompletableFuture;

public interface NetService extends EventBus<Packet, EventListener<? extends Packet>> {
    CompletableFuture<Void> sendPacket(Packet packet);
}
