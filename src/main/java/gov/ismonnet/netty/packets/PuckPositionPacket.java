package gov.ismonnet.netty.packets;

import gov.ismonnet.netty.core.Packet;
import gov.ismonnet.netty.core.PacketParser;
import io.netty.buffer.ByteBuf;

public class PuckPositionPacket implements Packet {

    private final float posX;
    private final float posY;

    private final float motionX;
    private final float motionY;

    public PuckPositionPacket(float posX, float posY, float motionX, float motionY) {
        this.posX = posX;
        this.posY = posY;
        this.motionX = motionX;
        this.motionY = motionY;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getMotionX() {
        return motionX;
    }

    public float getMotionY() {
        return motionY;
    }

    @Override
    public String toString() {
        return "PuckPositionPacket{" +
                "posX=" + posX +
                ", posY=" + posY +
                ", motionX=" + motionX +
                ", motionY=" + motionY +
                '}';
    }

    @Override
    public void writePacket(ByteBuf buf) {
        buf.writeFloat(posX);
        buf.writeFloat(posY);
        buf.writeFloat(motionX);
        buf.writeFloat(motionY);
    }

    public static final PacketParser<PuckPositionPacket> PARSER = buf ->
            new PuckPositionPacket(
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat(),
                    buf.readFloat());
}
