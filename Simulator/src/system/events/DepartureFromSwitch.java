package system.events;

import entities.*;
import system.*;
import system.utility.*;

public class DepartureFromSwitch extends Event {
	int dstNodeID;

	public DepartureFromSwitch(double startTime, int switchID, int dstNodeID, Packet packet) {
		super(Keywords.DepartureFromSwitch, startTime, switchID, packet);
		this.dstNodeID = dstNodeID;
	}

	/* --------------------------------------------------- */
	/* ---------- Inherited methods (from Event) --------- */
	/* --------------------------------------------------- */
	public Network execute(Network net) {
		net.updateTime(eventTime);
		net.switches.get(this.nodeID).releasePacket(net, dstNodeID, packet);
		return net;
	}
}
