package com.fsmeeting.zkclient.baisc.ops;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class CheckExists {

	public static void main(String[] args) {
		ZkClient zc = new ZkClient("192.168.7.178:2181",10000,10000,new SerializableSerializer());
		System.out.println("conneted ok!");
		
		boolean e = zc.exists("/fsmeeting/live/server/media");
		
		System.out.println(e);
		
	}
	
}
