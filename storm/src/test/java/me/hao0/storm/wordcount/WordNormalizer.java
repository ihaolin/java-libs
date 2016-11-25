/*
 * Copyright (c) 2016-2020 LEJR.COM All Right Reserved
 */

package me.hao0.storm.wordcount;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 词标准化器。
 */
public class WordNormalizer implements IRichBolt{
	
	private static final Logger logger = LoggerFactory.getLogger("WordCounter");

	private OutputCollector collector;
	@SuppressWarnings("rawtypes") 
	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);
		logger.info("line="+line);
		String words[] = line.split(" ");
		for(String word : words){
			word = word.trim();
			if(!word.isEmpty()){
				word = word.toLowerCase();
				//发出单词。
				List list = new ArrayList();
				list.add(input);
				collector.emit(list, new Values(word));
			}
		}
		// 完成tuple处理
		collector.ack(input);
	}

	@Override
	public void cleanup() {

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("word"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	private static final long serialVersionUID = 1L;
}
