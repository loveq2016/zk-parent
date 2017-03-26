package com.fsmeeting.zkclient.loadbalance.server;

public interface BalanceUpdateProvider {
	
	public boolean addBalance(Integer step);
	
	public boolean reduceBalance(Integer step);

}
