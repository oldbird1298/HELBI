package eu.oldbird.helbi.network;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents an inventory to handle the network nodes. It provides
 * convenient methods to solve problems.
 * 
 * @author dgerontop
 *
 */
public class Inventory {

	private List<Node> nodes;

	/**
	 * Empty constructor (default)
	 */
	public Inventory() {
		this.nodes = new ArrayList<>();
	}

	/**
	 * It construct a new inventory given the passed list of nodes
	 * 
	 * @param nodes
	 */
	public Inventory(List<Node> nodes) {
		this.nodes = nodes;
	}

	/**
	 * It returns the inventory as a list of nodes
	 * 
	 * @return
	 */
	public List<Node> getNodes() {
		return this.nodes;
	}

	public synchronized boolean addNode(Node node) {
		return this.nodes.add(node);
	}

	public synchronized Node getNode(int index) {
		return this.nodes.get(index);
	}

	public int getSize() {
		return this.nodes.size();
	}

	public void groupInventories(List<Node> list1, List<Node> list2) {
		Set<Node> set = new LinkedHashSet<>(nodes);
		set.addAll(list1);
		set.addAll(list2);
		this.nodes = new ArrayList<>(set);
	}

	public void printInventory() {
//		nodes.forEach(node -> System.out.println(node));
		for (Node node : nodes) {
			System.out.println((SimpleNode) node);
		}
	}

}
