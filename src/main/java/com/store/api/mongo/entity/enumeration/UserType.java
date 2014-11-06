package com.store.api.mongo.entity.enumeration;

public enum UserType {

	merchants, customer;

    public String getZh() {
        switch (this) {
        case merchants:
            return "商家";
        case customer:
            return "顾客";
        default:
            return "未知";
        }
    }
}
