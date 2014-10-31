package com.store.api.mongo.entity;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ConverterBean implements Serializable{

    private static final long serialVersionUID = 1704936225262833986L;
    
    @Id
    private Long id;
    
    private String name;
    
    private String desc;
    
    private boolean done;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public boolean isDone() {
        return done;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

}
