package system;

import entities.*;

public class Event {
	protected double time;
	protected String type;
	protected Flow flow;// This Flow does not preserve the STATE Variables. Be careful to use it as only
						// it is an ID!
	protected int packet_id;
	protected Node node; // This Node does not possess any STATE Variable. Be careful to use it as only
							// it is an ID!

	/*------------  Constant Values for different types of delays -----------*/
	protected final double CONTROLLER_RTT_DELAY = 0.0;
	protected final double CONTROLLER_PROCESS_DELAY = 0.0;
	/*-----------------------------------------------------------------------*/

	private double next_time;
	private String next_type;
	private Node next_node;

	public Event(double start_t, String type, Flow f, int p_id, Node node) {
		this.time = start_t;
		this.type = type;
		this.flow = f;
		this.packet_id = p_id;
		this.node = node;
	}

	public Network execute(Network net) {

		switch (this.type) {
		/* ################## First-Packet event ################### */
		case "First-Packet":
			System.out.println("First-Packet: Got it!");

			/* The first packet of the flow arrives to the first switch node */
			// The Node Sends a Request packet to the controller to get the forwarding rule
			// for the flow
			// This should be implemented as a public method in controller
			net = net.controller.newFlow(net, node, flow);
			// This communication means the next arrival event should be created with at the
			// time after this one. this should be considered somehow.
			//
			/* An Arrival event should be created */
			// It can be assumed that the switch has contacted the controller and the
			// timings have been considered.

			/* Updating next_time */
			// 1- There should be control_delay that represents the Ropund_trip travel of
			// control packet to the Controller
			next_time = next_time + CONTROLLER_RTT_DELAY;

			// 2- There should be a process_delay that represents the process time need for
			// the controller to decide what order to give to the requesting Node
			next_time = next_time + CONTROLLER_PROCESS_DELAY;

			/* Updating next_type */
			// The type of the next event is generic Arrival
			next_type = "First-Packet";

			/* Updating flow, packet_id and next_node(or Link?) */
			next_node = node.getEgressLink(flow.getDst()).getDst(); // There is a possibility that the Dst node of the
																	// desired Link to be the same as the Current Node
																	// that we have called its getEgressLink(). One way
																	// to solve this issue is to give the Current Node
																	// to the Link.getDst() method and complete the
																	// checking in that method.

			// Generate next arrival event
			net.event_List.generateEvent(next_time, next_type, flow, packet_id, next_node);

			/* ################## Arrival event ######################## */
		case "Arrival":
			System.out.println("Arrival: Got it!");

			/* Checking the occupancy of the buffer upon packet arrival */
			if (net.nodes.get(node.getLabel()).getBuffer().isFull()) {

				/* Generating a Drop event */
				// There is no need for Drop event. We can update statistical counters
				// Also, there should be a mechanism to manage timers and time-outs of the
				// Transport layer protocols. But it's not here (I suppose).

			} else { // The buffer has available space

				/* Check if the packet has arrived to the destination */
				if (net.flows.get(flow.getLabel()).hasArrived(net.nodes.get(node.getLabel()))) {

					/*
					 * Informing the flow agent that the packet has arrived - using listener()
					 * method
					 */
					net = net.flows.get(flow.getLabel()).dst_agent.listener(net, "recv");
				} else {// The packet is ready for the departure
					/* Generating next Arrival event */

					// Getting next_node_id

					/* Calculating all types of delays for the packet */
					// 1- Queuing Delay
					// Getting wait time from the buffer
					Double queue_delay = net.nodes.get(node.getLabel()).getBuffer().getWaitTime();

					// 2- Processing Delay
					Double process_delay = 0.0; // Some constant that should be set by the simulator settings
					net.nodes.get(node.getLabel()).getBuffer()
							.updateDepartureTime(this.time + process_delay + queue_delay);
					// 3-Propagation Delay 4- Transmission Delay
					Double prob_delay = net.nodes.get(node.getLabel()).neighbors.get(next_node).getPropagationDelay();
					Double trans_delay = net.nodes.get(node.getLabel()).neighbors.get(next_node)
							.getTransmissionDelay(net.flows.get(flow.getLabel()).getPacketSize());

					// Calculating next_time
					next_time = this.time + queue_delay + process_delay + prob_delay + trans_delay;

					// Generate next arrival event
					net.event_List.generateEvent(next_time, next_type, flow, packet_id, next_node);
				}

			}
			/* Update the Log */

			break;
		case "Departure":
			System.out.println("Departure: Got it!");
			// Right now we do not need Departure event
			break;
		case "Drop":
			System.out.println("Drop: Got it!");
			// Right now we do not need Drop event
			break;
		default:
			System.out.println("Error: Event.run() - Invalid event type (" + this.type + ")");
			break;
		}
		return net;

	}
}
