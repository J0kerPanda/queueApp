package ru.bmstu.queueapp.http;

public interface ResponseHandler<T> {
    void handle(T result);
}