package com.store.api.utils.http;

import org.apache.http.HttpEntity;

public class HttpCallResult {
    private int statusCode;
    private HttpEntity httpEntity;
    private String content;
    
    public int getStatusCode() {
        return statusCode;
    }
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public HttpEntity getHttpEntity() {
        return httpEntity;
    }
    public void setHttpEntity(HttpEntity httpEntity) {
        this.httpEntity = httpEntity;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    
    
    
}
