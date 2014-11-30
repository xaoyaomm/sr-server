/**
 * UserSearch.java
 *
 * Copyright 2014 redmz, Inc. All Rights Reserved.
 *
 * created by vincent 2014年11月29日
 */
package com.store.api.mongo.entity.vo;

import com.store.api.mongo.entity.User;

/**
 * 
 * Revision History
 * 
 * 2014年11月29日,vincent,created it
 */
public class UserSearch implements Comparable<UserSearch>{

    /** 送货点与收货点的距离（单位：米） **/
    private long distance=0;
    
    /** 用户对象 **/
    private User user;

    public long getDistance() {
        return distance;
    }

    public User getUser() {
        return user;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int compareTo(UserSearch o) {
        if (this.distance>o.distance) {
            return 1;
        }
        if (this.distance<o.distance) {
            return -1;
        }
        return 0;
    }
    
    
    
}
