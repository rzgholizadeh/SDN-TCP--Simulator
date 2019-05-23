package entities;

import buffers.Bufferv1;
import system.Keywords;

/** Links are attributes of Switch and Host **/
/** There are two types of link: 1.accessLink 2.networkLink **/

public class Link extends Entity {

	public Buffer buffer;

	private int bandwidth;
	private double propDelay;
	private int srcNodeID;
	private int dstNodeID;

	public Link(int ID, int sourceID, int destinationID, double propagationDelay, int band, int bufferSize,
			int bufferPolicy) {
		super(ID);
		this.bandwidth = band; // Mega_bits/second
		this.propDelay = propagationDelay; // millisecond
		this.srcNodeID = sourceID;
		this.dstNodeID = destinationID;

		buffer = new Bufferv1(bufferSize, bufferPolicy);
	}

	public double getTotalDelay(int segmentSize) {
		return this.getTransmissionDelay(segmentSize) + this.propDelay;
	}

	public double getTransmissionDelay(int segmentSize) {
		Double trans_delay = segmentSize / (double) this.bandwidth;
		return trans_delay;
	}

	/*------------------- Getters and Setters  ------------------------*/
	public int getBandwidth() {
		return this.bandwidth;
	}

	public Double getPropagationDelay() {
		return this.propDelay;
	}

	public int getSrcID() {
		return this.srcNodeID;
	}

	public int getDstID() {
		return this.dstNodeID;
	}
	/*--------------------------------------------------------------*/
}
