package com.store.api.mongo.entity.enumeration;

public enum UserType {

	merchants, customer, temp;

    public String getZh() {
        switch (this) {
        case merchants:
            return "商家";
        case customer:
            return "顾客";
        case temp:
            return "临时用户";
        default:
            return "未知";
        }
    }
}
