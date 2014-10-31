package com.store.api.session;

public class SessionUserType {
    private final String type;
    
    protected SessionUserType(String type){
        this.type=type;
    }
    
    protected String getType() {
        return type;
    }

}
