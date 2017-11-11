package io.pkts.framer;

import java.io.IOException;

import io.pkts.buffer.Buffer;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.rtp.impl.RtpPacketImpl;
import io.pkts.packet.rtps.RtpsPacket;
import io.pkts.packet.rtps.impl.RtpsPacketImpl;
import io.pkts.protocol.Protocol;

public class RTPSFramer implements Framer<TransportPacket, RtpsPacket> {

	@Override
	public Protocol getProtocol() {
        return Protocol.RTPS;
	}

	@Override
	public RtpsPacket frame(TransportPacket parent, Buffer buffer) throws IOException, FramingException {
        if (parent == null) {
            throw new IllegalArgumentException("The parent frame cannot be null");
        }

        final int index = buffer.getReaderIndex();

        try {
            final Buffer headers = buffer.readBytes(16);
            final Buffer payload = buffer.slice(buffer.capacity());
            return new RtpsPacketImpl(parent, headers, payload);
        } catch (final IndexOutOfBoundsException e) {
            buffer.setReaderIndex(index);
            throw e;
        }
	}

	@Override
	public boolean accept(Buffer data) throws IOException {
        return couldBeRTPSMessage(data);
	}

	private boolean couldBeRTPSMessage(Buffer data) throws IOException {
        if (data.getReadableBytes() < 4) {
            return false;
        }

        final byte a = data.getByte(0);
        final byte b = data.getByte(1);
        final byte c = data.getByte(2);
        final byte d = data.getByte(3);
        return a == 'R' && b == 'T' && c == 'P' && (d == 'S' || d == 'X');
	}
}
