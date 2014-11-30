package com.store.api.mongo.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 给Mongo增加自增主键
 */
@Document(collection="global_sequence")
public class Sequence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5075732214984800587L;

	@Id
	private long id=0;
	/**
	 *  主键
	 */
	private long seq=1;
	/**
	 * 步长
	 */
	private long step=1;
	/**
	 * 序列名字
	 */
	private  String name="";
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getSeq() {
		return seq;
	}
	public void setSeq(long seq) {
		this.seq = seq;
	}
	public long getStep() {
		return step;
	}
	public void setStep(long step) {
		this.step = step;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
