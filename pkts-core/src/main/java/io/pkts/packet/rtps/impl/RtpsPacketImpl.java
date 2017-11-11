package io.pkts.packet.rtps.impl;

import java.io.IOException;
import java.io.OutputStream;

import io.pkts.buffer.Buffer;
import io.pkts.packet.Packet;
import io.pkts.packet.PacketParseException;
import io.pkts.packet.TransportPacket;
import io.pkts.packet.impl.AbstractPacket;
import io.pkts.packet.rtps.RtpsPacket;
import io.pkts.protocol.Protocol;

public class RtpsPacketImpl extends AbstractPacket implements RtpsPacket {

	private Packet parent;

	public RtpsPacketImpl(TransportPacket parent, Buffer headers, Buffer payload) {
        super(Protocol.RTPS, parent, payload);
        this.parent = parent;
        // this.headers = headers;
        // this.payload = payload;
	}

	@Override
	public long getArrivalTime() {
		return this.parent.getArrivalTime();
	}

	@Override
	public void write(OutputStream out, Buffer payload) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Packet getNextPacket() throws IOException, PacketParseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Packet clone() {
		// TODO Auto-generated method stub
		return null;
	}

}
