/*
 * Copyright (c) 2016-2020 LEJR.COM All Right Reserved
 */

package me.hao0.storm.wordcount;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * 主程序。
 * 
 * @author  WuHong 
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {

		//定义拓扑
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		builder.setBolt("word-counter", new WordCounter()).fieldsGrouping("word-normalizer", new Fields("word"));

		//配置
		Config config = new Config();
		config.setDebug(false);

		//运行拓扑
		config.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

		//创建本地集群
		LocalCluster cluster = new LocalCluster();

		cluster.submitTopology("Getting-Started-Topology", config, builder.createTopology());

		try {
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//关闭本地集群
		cluster.shutdown();
	}

}
