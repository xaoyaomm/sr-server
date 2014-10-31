package com.store.api.mongo.entity;

import java.io.Serializable;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 给Mongo增加自增主键
 * @author @haipenge 
 * haipenge@gmail.com
*  Create Date:2014年8月10日
 */
@Document(collection="global_sequence")
public class Sequence implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5075732214984800587L;

	@Id
	private Long id=null;
	/**
	 *  主键
	 */
	private Long seq=new Long(1);
	/**
	 * 步长
	 */
	private Long step=new Long(1);
	/**
	 * 序列名字
	 */
	private  String name="";
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public Long getStep() {
		return step;
	}
	public void setStep(Long step) {
		this.step = step;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
