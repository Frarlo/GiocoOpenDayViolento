package gov.ismonnet.netty;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketIdService;
import gov.ismonnet.netty.core.PacketParser;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PacketIdServiceImpl implements PacketIdService {

    private final Map<Class<? extends Packet>, Byte> packetIds;
    private final Map<Byte, PacketParser> idToParsers;

    @SuppressWarnings("unchecked")
    @Inject PacketIdServiceImpl(Map<Class<? extends Packet>, Byte> packetIds,
                                Map<Class<?>, PacketParser> parsersMap) {

        this.packetIds = packetIds;

        final Map<Byte, PacketParser> tempParsers = new HashMap<>();
        parsersMap.forEach((packet, parser) -> tempParsers.put(getPacketId((Class<? extends Packet>) packet), parser));
        this.idToParsers = Collections.unmodifiableMap(tempParsers);
    }

    @Override
    public byte getPacketId(Class<? extends Packet> clazz) {
        return packetIds.get(clazz);
    }

    @Override
    public PacketParser getParserById(byte id) {
        return idToParsers.get(id);
    }
}
