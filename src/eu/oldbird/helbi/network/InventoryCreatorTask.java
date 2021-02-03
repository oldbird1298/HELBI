package eu.oldbird.helbi.network;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InventoryCreatorTask extends RecursiveTask<Inventory> {

	private List<InetAddress> inetAddresses;
	private Inventory list;
	private ThreadPoolExecutor executor;
	private int hosts;

	public InventoryCreatorTask(List<InetAddress> list) {
		this.inetAddresses = list;
		this.hosts = list.size();
		this.list = new Inventory();
	}

	@Override
	protected Inventory compute() {

		if (hosts < 8) {
//			list = createNodes(this.inetAddresses);
			list = createNodesMultiThread(this.inetAddresses);
		} else {
			InventoryCreatorTask task1 = new InventoryCreatorTask(
					inetAddresses.subList(0, (inetAddresses.size() - 1) / 2));
			InventoryCreatorTask task2 = new InventoryCreatorTask(
					inetAddresses.subList(((inetAddresses.size() - 1) / 2), inetAddresses.size()));
			invokeAll(task1, task2);

			try {
				list.groupInventories(task1.get().getNodes(), task2.get().getNodes());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}

		return list;
	}

	private Inventory createNodes(List<InetAddress> inetAddresses) {

		for (int i = 0; i < this.hosts; i++) {
			list.addNode(new SimpleNode(inetAddresses.get(i).getHostName(), inetAddresses.get(i).getHostAddress()));
		}
		return list;
	}

	private Inventory createNodesMultiThread(final List<InetAddress> inetAddresses) {

		this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(hosts);
		List<Future<Node>> futures = new ArrayList<>();

		for (InetAddress addr : inetAddresses) {
			System.out.printf("Creating/Inserting host to inventory: %s\n", addr.getHostAddress());
			Future<Node> future = this.executor.submit(() -> {
				Node node = new SimpleNode(addr.getHostName(), addr.getHostAddress());
				return node;
			});
			futures.add(future);
		}

		do {
			System.out.printf("Number of Completed Tasks: %d\n", this.executor.getCompletedTaskCount());
//			for(Future<Node> future: futures) {
//				System.out.printf("Task completed:  %s\n", future.isDone());
//			}
			try {
				TimeUnit.MILLISECONDS.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (executor.getCompletedTaskCount() < futures.size());

		for (Future<Node> future : futures) {
			try {
				list.addNode(future.get());
			} catch (InterruptedException | ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		executor.shutdown();
		return list;

	}

}
