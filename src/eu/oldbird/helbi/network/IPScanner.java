/**
 * 
 */
package eu.oldbird.helbi.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

/**
 * @author dgerontop
 *
 */
public class IPScanner implements NodeListBuilder {

	private final static int START_FLAG = -128;

	private Inventory nodes;
	private final ForkJoinPool commonPool;
	private int hosts;
	private String ip;
	private int mask;
	private List<InetAddress> addresses;

	//This is not the proper constructor
	public IPScanner() {
		this.nodes = new Inventory();
		this.commonPool = ForkJoinPool.commonPool();
	}

	public IPScanner(String ip, int mask) {
		this.nodes = new Inventory();
		this.hosts = IPScanner.calculateHosts(mask);
		this.ip = ip;
		this.mask = mask;
		this.commonPool = ForkJoinPool.commonPool();
	}
	
	public IPScanner(Inventory inventory) {
		this.commonPool = ForkJoinPool.commonPool();
		this.nodes = inventory;
	}

	/**
	 * Trying to find the hosts
	 */
	@Override
	public NodeListBuilder startupOp() {
		byte[] net;
		try {
			net = IPScanner.calculateNetwork(this.ip, this.mask);
			InetAddress network = InetAddress.getByAddress(net);
			this.addresses = IPScanner.findAllAddresses(network, this.mask);
			System.out.println(this.addresses.size());
			//TODO replace this fault tollerance code
			InventoryCreatorTask task = new InventoryCreatorTask(addresses);
			this.commonPool.execute(task);
			
			
			do {
				System.out.printf("***********************************************\n");
				System.out.printf("Active Threads: %d\n", commonPool.getActiveThreadCount());
				System.out.printf("Task count: %d\n", commonPool.getQueuedTaskCount());
				System.out.printf("Steal count: %d\n", commonPool.getStealCount());
				System.out.printf("***********************************************\n");
				try {
					TimeUnit.SECONDS.sleep(1);
					
				}catch (InterruptedException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			} while(!task.isDone());
			
			this.nodes = task.get();
			commonPool.shutdown();
			
//			this.addresses.forEach((inet) -> {
//				System.out.println("Creating new node " + inet.getHostAddress());
//				Node node = new SimpleNode(inet.getHostName(), inet.getHostAddress());
//				this.nodes.addNode(node);
//			});
		} catch (UnknownHostException | ExecutionException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//			System.out.println(e.getMessage());
		}
		return this;
	}

	@Override
	public NodeListBuilder networkScanOp() {
		// TODO Auto-generated method stub
		System.out.println("Start comprehensive scan of " + this.hosts);
//		Long startTime = System.nanoTime();

//		this.nodes.getNodes().forEach(node -> {
//			executor.submit(() -> {
//
//				if (node.isReachable()) {
//					((SimpleNode) node).setOnline(true);
//				}
//
//			});
//
//		});
//		this.executor.shutdown();
//		Long endTime = System.nanoTime();
//		System.out.println("Scanning " + this.hosts + " in " + ((endTime - startTime) / 1000000000.0) + "s");
		return this;
	}

	@Override
	public NodeListBuilder discoveryOp() {
		// TODO Auto-generated method stub
		System.out.println("Printing");
		this.nodes.printInventory();
		return this;
	}

	@Override
	public void addProperties() {
		// TODO Auto-generated method stub

	}

	@Override
	public Inventory getNodes() {
		// TODO Auto-generated method stub
		return this.nodes;
	}

	/**
	 * It calculate the number of the hosts available in according to a given
	 * netmask number
	 * 
	 * @param maskLength
	 * @return The number of the hosts
	 */
	public static int calculateHosts(int maskLength) {
		int hosts = (int) Math.pow(2, 32 - maskLength) - 2;
		return hosts;
	}

	/**
	 * This static method return the Netmask in byte array
	 * 
	 * @param maskLen
	 * @return
	 */
	public static byte[] calculateNetmask(int maskLen) {
		int bytes = maskLen / 8;
		int bits = maskLen % 8;
		byte[] subnetmask = new byte[4];
		for (int i = bytes; i > 0; i--) {
			subnetmask[i - 1] = (byte) 0xff;
		}
		if (bits > 0) {
			subnetmask[bytes] = (byte) (START_FLAG >> (bits - 1) & 0xff);
		}
		return subnetmask;
	}

	/**
	 * It return the network in byte[] according to the given IP and predefined
	 * netmask
	 * 
	 * @param ipAddress
	 * @param maskLen
	 * @return
	 * @throws UnknownHostException
	 */
	public static byte[] calculateNetwork(String ipAddress, int maskLen) throws UnknownHostException {
		System.out.println("Calculate network: " + ipAddress);
		byte[] network = new byte[4];
		byte[] netmask = IPScanner.calculateNetmask(maskLen);
		InetAddress ip = InetAddress.getByName(ipAddress);
		byte[] rawIP = ip.getAddress();
		for (int i = 0; i < network.length; i++) {
			network[i] = (byte) (rawIP[i] & netmask[i]);

		}
		return network;
	}

	/**
	 * It returns a list of InetAddresses according to the given network and mask
	 * 
	 * @param network
	 * @param maskLen
	 * @return
	 * @throws UnknownHostException
	 */
	public static ArrayList<InetAddress> findAllAddresses(InetAddress network, int maskLen)
			throws UnknownHostException {
		System.out.println("Trying to calculate addresses to ping");
		ArrayList<InetAddress> inets = new ArrayList<>();
		int hosts = IPScanner.calculateHosts(maskLen);
		ByteBuffer hostBits = ByteBuffer.wrap(network.getAddress());
		int hostAddress = hostBits.getInt();
		int counter = 1;
		while (counter <= hosts) {
			hostAddress++;
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.putInt(hostAddress);
			inets.add(InetAddress.getByAddress(bb.array()));
			counter++;
		}
		return inets;
	}

	public static InetAddress findLastAddress(InetAddress network, int maskLen) throws UnknownHostException {
		if (maskLen == 32) {
			return network;
		}
		ByteBuffer netWrapped = ByteBuffer.wrap(network.getAddress());
		int lastAddress = netWrapped.getInt() + IPScanner.calculateHosts(maskLen);
		ByteBuffer lib = ByteBuffer.allocate(4);
		lib.putInt(lastAddress);
		return InetAddress.getByAddress(lib.array());

	}

	public static InetAddress findFirstAddress(InetAddress network, int maskLen) throws UnknownHostException {
		if (maskLen == 32) {
			return network;
		}
		ByteBuffer netWrapped = ByteBuffer.wrap(network.getAddress());
		int fistAddress = netWrapped.getInt() + 1;
		ByteBuffer fib = ByteBuffer.allocate(4);
		fib.putInt(fistAddress);
		return InetAddress.getByAddress(fib.array());

	}

}
