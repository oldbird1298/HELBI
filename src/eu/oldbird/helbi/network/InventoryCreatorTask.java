package eu.oldbird.helbi.network;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RecursiveTask;

public class InventoryCreatorTask extends RecursiveTask<Inventory> {
	
	private List<InetAddress> inetAddresses;
	private Inventory list;
	private int hosts;
	
	public InventoryCreatorTask(List<InetAddress> list) {
		this.inetAddresses = list;
		this.hosts = list.size();
		this.list = new Inventory();
	}

	
	@Override
	protected Inventory compute() {
		
		
		if (hosts < 8) {
			list = createNodes(this.inetAddresses);
		} else {
			InventoryCreatorTask task1 = new InventoryCreatorTask(inetAddresses.subList(0, (inetAddresses.size() - 1) / 2));
			InventoryCreatorTask task2 = new InventoryCreatorTask(inetAddresses.subList((inetAddresses.size()/2), inetAddresses.size() -1));
			invokeAll(task1, task2);
			
			try {
				list.groupInventories(task1.get().getNodes(), task2.get().getNodes());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			
			
		}
		
		return list;
	}


	private Inventory createNodes(List<InetAddress> inetAddresses2) {
		
		for (int i = 0; i < this.hosts ; i++) {
			list.addNode(new SimpleNode(inetAddresses.get(i).getHostName(), inetAddresses.get(i).getHostAddress()));
		}
		return list;
	}
	
	

}
