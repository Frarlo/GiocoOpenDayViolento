package gov.ismonnet.netty.core;

import gov.ismonnet.event.EventBus;
import gov.ismonnet.event.EventListener;

import java.util.concurrent.Future;

public interface NetService extends EventBus<Packet, EventListener<? extends Packet>> {
    Future<Void> sendPacket(Packet packet);
}
