/*
 * Copyright (c) 2016-2020 LEJR.COM All Right Reserved
 */

package me.hao0.storm.wordcount;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import java.io.*;
import java.util.Map;

/**
 * 数据源
 */
public class WordReader implements IRichSpout{

	//数据文件路径。
	private static final String DATA_FILE_PATH = "/words.txt";

	private InputStreamReader source;

	private SpoutOutputCollector collector;

	private boolean completed = false;

	@SuppressWarnings("rawtypes") 
	@Override
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		source = new InputStreamReader(getClass().getResourceAsStream(DATA_FILE_PATH));
		this.collector = collector;
	}
	@Override
	public void close() {
		try {
			source.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void activate() {
	}
	@Override
	public void deactivate() {
	}
	@Override
	public void nextTuple() {
		if(completed){
			return;
		}
		String line;
		BufferedReader reader = new BufferedReader(source);
		try {
			while((line = reader.readLine()) != null){
				//发出数据。
				this.collector.emit(new Values(line), line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			completed = true;
		}
	}

	@Override
	public void ack(Object msgId) {
	}

	@Override
	public void fail(Object msgId) {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}
	@Override
	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

	private static final long serialVersionUID = 1L;
}
