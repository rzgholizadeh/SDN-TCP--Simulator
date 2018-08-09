package network;

public class Link {
	private double bandwidth;
	private double propagation_delay;
	private Node source;
	private Node destination;
	String label;

	public Link(String lable, Node src, Node dest, Double prop_del, Double band) {
		this.label = label;
		this.bandwidth = band;
		this.propagation_delay = prop_del;
		this.source = src;
		this.destination = dest;
	}

	public void setBandwidth(Double b) {
		this.bandwidth = b;
	}

	public Double getBandwidth() {
		return this.bandwidth;
	}

	public void setPropagationDelay(Double pd) {
		this.propagation_delay = pd;
	}

	public Double getPropagationDelay() {
		return this.propagation_delay;
	}

	public void setSrc(Node src) {
		this.source = src;
	}

	public Node getSrc() {
		return this.source;
	}

	public void setDst(Node dst) {
		this.destination = dst;
	}

	public Node getDst() {
		return this.destination;
	}
}