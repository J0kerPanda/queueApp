package ru.bmstu.queueapp.http.error;

import org.jetbrains.annotations.Nullable;

public interface ErrorHandler<T> {

    void handle(int code, Throwable e, @Nullable T response);
}
