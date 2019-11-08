package gov.ismonnet.netty.core;

import java.util.concurrent.Future;

public interface NetService {
    Future<Void> sendPacket(Packet packet);
}
