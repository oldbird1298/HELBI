package eu.oldbird.helbi.network;

import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadPoolExecutor;

public class PingTesterTask extends RecursiveAction {

	private Inventory inv;
	private int startIndex;
	private int endIndex;
//	private ThreadPoolExecutor executor;

	public PingTesterTask(Inventory inventory, int startIndex, int endIndex) {
		this.inv = inventory;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	protected void compute() {
		System.out.println("From " + startIndex + " to: " + endIndex );
		if ((endIndex - startIndex) < 8) {
			pingHosts();
		} else {
			int middle = (startIndex + endIndex) / 2;
			PingTesterTask task1 = new PingTesterTask(this.inv, startIndex, middle + 1);
			PingTesterTask task2 = new PingTesterTask(this.inv, middle + 1, endIndex);
			System.out.println("Pending tasks " + getQueuedTaskCount());
			
			invokeAll(task1, task2);
		}
	}

	private void pingHosts() {
		for (int i = startIndex; i < endIndex; i++) {
			Node node = inv.getNode(i);
			System.out.println("Pinging " + node.getInetAddress().getHostAddress());
			if (node.isReachable()) {
				((SimpleNode)node).setOnline(true);
			}
		}
	}

}
