/**
 * 
 */
package eu.oldbird.helbi.network;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 
 * @author dgerontop
 *
 */
public abstract class AbstractNode implements Node {

	private static Long SERIAL_UID = 1L;
	protected final static int RETRIES = 3;

	private final Long id;
	private String name;
	private InetAddress address;
	protected boolean online;
	protected boolean isReachable;
	private HashMap<String, Object> properties;
	private Calendar timestamp;

	public AbstractNode() {
		this.id = SERIAL_UID++;
	}

	public AbstractNode(String ipAddress) {
		this.id = SERIAL_UID++;
		try {

			this.address = InetAddress.getByName(ipAddress);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public AbstractNode(String ipAddress, String name) {
		this.name = name;
		this.id = SERIAL_UID++;
		try {

			this.address = InetAddress.getByName(ipAddress);

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Long getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public InetAddress getInetAddress() {
		return this.address;
	}

	public Inet6Address getInet6Address() {
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isOnline() {
		return isReachable();
	}

	public void setOnline(boolean online) {
		this.online = online;
	}
	
	
	public HashMap<String, Object> getProperties() {
		return this.properties;
	}

	
	public Object getProperty(String key) {
		return this.properties.get(key);
	}
	
	public String getStringProperty(String key) {
		return this.properties.get(key).toString();
	}
	
	public void setProperties(HashMap<String, Object> properties) {
		
		this.properties = properties;
	}
	/**
	 * {@inheritDoc}
	 * 
	 */
	public abstract boolean isReachable();

	public String toString() {
		return String.format("Node id:%s\nNode name:%s\nNode IP:%s\nOnline: %s\n", this.id, this.name, this.address.getHostAddress() ,this.online);
	}

}
