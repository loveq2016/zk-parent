package com.fsmeeting.zkclient.loadbalance.client;

import java.util.List;

public interface BalanceProvider<T> {
	
	public T getBalanceItem();
	

}
