package eu.oldbird.helbi.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.xml.stream.events.StartDocument;

public class App {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Hello Helbi");
		System.setProperty("java.net.preferIPv4Stack" , "true");
		int nThreads = Runtime.getRuntime().availableProcessors();
		System.out.println("Available processers: " + nThreads);
		
//		try {
//			Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
//			List<NetworkInterface> interfaces = Collections.list(nets);
//			for (NetworkInterface inet : interfaces) {
//				displayInterfaceInformation(inet);
//			}
//		} catch (SocketException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Inventory list = new Inventory();
//		List<Node> nodes = new ArrayList<>();
//		list.addNode(new SimpleNode("Oldbird-PC", "192.168.1.2"));
//		list.addNode(new SimpleNode("Oldbird-Laptop", "192.168.1.3"));
//		list.addNode(new SimpleNode("DockerStation", "192.168.1.4"));
//		list.addNode(new SimpleNode("Unknown-1", "192.168.1.43"));
//		
//		list.printInventory();
//		list.getNodes().forEach(node -> {
//			if (node.isReachable()) {
//				System.out.println(node.getInetAddress().getHostName() +  " is online");
//				((SimpleNode)node).setOnline(true);
//				
//			}else {
//				System.out.println(node.getInetAddress().getHostName() + " is not online");
//			}
//		});
		
//		for(;;) {
//			System.out.println("Echo");
//		}
		
		Long statTime = System.nanoTime();
		
		Inventory inv = new IPScanner("192.168.1.7", 24)
				.startupOp()
				.networkScanOp()
				.discoveryOp()
				.getNodes();
		inv.printInventory();
		System.out.println(inv.getSize());
		Long endTime = System.nanoTime();
		System.out.println("Number of hosts for netmask /" + 24 +  " " + IPScanner.calculateHosts(24));
		System.out.println("IPScanner took " + ((endTime - statTime) / 1000000000.0) + " s for scanning " + inv.getSize() + " hosts");
				
	}
	
	static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		System.out.printf("Display Name: %s\n", netint.getDisplayName());
		System.out.printf("Name: %s\n", netint.getName());
		System.out.printf("Virtual: %s\n", netint.isVirtual());
		System.out.printf("P2P: %s\n", netint.isPointToPoint());
		System.out.printf("UP: %s\n", netint.isUp());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			System.out.printf("InetAddress: %s\n", inetAddress );
		}
	}
	
	
	

}
