package com.store.api.mongo.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class OnlineTime implements Serializable{

    private static final long serialVersionUID = -5660874077952891900L;
    
    /**
     * 车主ID
     */
    @Id
    private Long id;
    
    /**
     * 积分发放次数(每天清零)
     */
    private long giveCount=0;
    
    /**
     * 当天最早坐标上传时间
     */
    private Long firstTime;
    
    /**
     * 最后一次发放积分的时间
     */
    private Long lastTime;

    public Long getId() {
        return id;
    }

    public long getGiveCount() {
        return giveCount;
    }

    public Long getFirstTime() {
        return firstTime;
    }

    public Long getLastTime() {
        return lastTime;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGiveCount(long giveCount) {
        this.giveCount = giveCount;
    }

    public void setFirstTime(Long firstTime) {
        this.firstTime = firstTime;
    }

    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }


}
