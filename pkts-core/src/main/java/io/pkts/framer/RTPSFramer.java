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

            // An RTP packet has a least 12 bytes but can contain more depending on
            // extensions, padding etc. Figure that out.
            final Buffer headers = buffer.readBytes(12);
            final Byte b = headers.getByte(0);
            final boolean hasPadding = (b & 0x20) == 0x020;
            final boolean hasExtension = (b & 0x10) == 0x010;
            final int csrcCount = b & 0x0F;

            if (hasExtension) {
                final short extensionHeaders = buffer.readShort();
                final int length = buffer.readUnsignedShort();
                final Buffer extensionData = buffer.readBytes(length);
            }

            if (hasPadding || hasExtension || csrcCount > 0) {
                // throw new RuntimeException("TODO - have not implemented the case of handling padding, extensions etc");
            }
            final Buffer payload = buffer.slice();
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
        return a == 'R' && b == 'T' && c == 'P' && d == 'S';
	}
}
