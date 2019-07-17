package com.loser.sharefile.controller.result;

public class ResponseEntity<T> {
    private int status;
    private T body;

    public ResponseEntity(T body, int status) {
        this.status = status;
        this.body = body;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
