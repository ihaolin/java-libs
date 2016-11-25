/*
 * Copyright (c) 2016-2020 LEJR.COM All Right Reserved
 */

package me.hao0.storm.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 数词器。
 * 
 * @author  WuHong 
 * @version 1.0
 */
public class WordCounter implements IRichBolt{
	
	private static final Logger logger = LoggerFactory.getLogger("WordCounter");

	/**
	 * 统计器。
	 */
	private Map<String , Integer> counters;

	private OutputCollector collector;

	@SuppressWarnings("rawtypes")
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.counters = new HashMap<>();
	}

	@Override
	public void execute(Tuple input) {
		String word = input.getString(0);
		if(!counters.containsKey(word)){
			counters.put(word, 1);
		}else{
			Integer count = counters.get(word) + 1;
			counters.put(word, count);
		}

		// 完成tuple处理
		collector.ack(input);
	}

	@Override
	public void cleanup() {
		logger.info("=================单词数量====================");
		for(String key : counters.keySet()){
			logger.info("单词["+key+"]的数量为["+counters.get(key)+"]!");
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	private static final long serialVersionUID = 1L;
}
