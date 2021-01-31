package eu.oldbird.helbi.network;

import java.io.IOException;

public class SimpleNode extends AbstractNode {
	
	
	public SimpleNode(String name, String ipAddress) {
		
		super(ipAddress, name);
		
	}

	@Override
	public boolean isReachable() {
		
		try {
			return this.getInetAddress().isReachable(5000);
		} catch (IOException e) {
			
//			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return false;
	}
	

}
