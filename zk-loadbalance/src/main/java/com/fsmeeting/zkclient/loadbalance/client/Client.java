package com.fsmeeting.zkclient.loadbalance.client;

public interface Client {

	public void connect() throws Exception;
	public void disConnect() throws Exception;
	
}
