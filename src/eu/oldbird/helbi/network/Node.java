/**
 * 
 */
package eu.oldbird.helbi.network;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.util.HashMap;

/**
 * <strong>Class</strong>Node Represents a logical network node which is
 * reachable through the network.
 * 
 * @author dgerontop <Dimitrios Gerontopoulos>
 *
 */
public interface Node {

	/**
	 * It return the InetAddress. Every node must have an inet address version 4
	 * 
	 * @return
	 */
	public InetAddress getInetAddress();

	/**
	 * It return the InetAddress. Optional if the specific node has an IPv6.
	 * 
	 * @return
	 */
	public Inet6Address getInet6Address();

	/**
	 * It returns the name of this node
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * It return the propertiest of this node
	 * 
	 * @return
	 */
	public HashMap<String, Object> getProperties();

	/**
	 * It sets the name of this node
	 * 
	 * @param name
	 */
	public void setName(String name);

	/**
	 * Check wether this node is online
	 * 
	 * @return
	 */
	public boolean isOnline();

	/**
	 * Check if node is reachable via current node
	 * 
	 * @return
	 */
	public boolean isReachable();

	/**
	 * Gets the ID of Node
	 * 
	 * @return
	 */
	public Long getId();

	/**
	 * Set the additional properties of Node
	 * 
	 * @param properties
	 */
	public void setProperties(HashMap<String, Object> properties);

}
