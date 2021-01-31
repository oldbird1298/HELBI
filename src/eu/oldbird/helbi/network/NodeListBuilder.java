package eu.oldbird.helbi.network;

public interface NodeListBuilder {

	NodeListBuilder startupOp();

	NodeListBuilder networkScanOp();

	NodeListBuilder discoveryOp();

	void addProperties();

	Inventory getNodes();

}
