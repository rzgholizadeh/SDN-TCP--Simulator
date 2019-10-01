package simulator.events;

import simulator.Event;
import simulator.Network;
import simulator.Simulator;
import utility.Debugger;
import utility.Keywords;

public class TimeoutEvent extends Event {
	private int nodeID;
	private int timerID;

	public TimeoutEvent(float eventTime, int nodeID, int timerID) {
		super(eventTime);
		this.nodeID = nodeID;
		this.timerID = timerID;
	}

	@Override
	public void execute(Network net) {
		//Debugger.debugEvent("TimeOut", eventTime, null);
		net.updateTime(eventTime);
		short nodeType = Simulator.getNodeType(nodeID);
		switch (nodeType) {
		case Keywords.Entities.Nodes.Types.Controller:
			net.controllers.get(nodeID).executeTimeOut(net, timerID);
			break;
		case Keywords.Entities.Nodes.Types.SDNSwitch:
			net.switches.get(nodeID).executeTimeOut(net, timerID);
			break;
		case Keywords.Entities.Nodes.Types.Host:
			net.hosts.get(nodeID).executeTimeOut(net, timerID);
			break;
		default:
			break;
		}
	}

	public int getTimerID() {
		return timerID;
	}

}