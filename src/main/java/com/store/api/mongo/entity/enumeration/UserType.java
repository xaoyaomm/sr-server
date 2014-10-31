package com.store.api.mongo.entity.enumeration;

public enum UserType {

    owners, cargo, staff;

    public String getZh() {
        switch (this) {
        case owners:
            return "车主";
        case cargo:
            return "货主";
        case staff:
            return "业务员";
        default:
            return "未知";
        }
    }
}
