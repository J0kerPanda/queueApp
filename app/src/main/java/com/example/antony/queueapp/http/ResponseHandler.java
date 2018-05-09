package com.example.antony.queueapp.http;

public interface ResponseHandler<T> {
    void handle(T result);
}