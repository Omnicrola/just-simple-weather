package com.omnicrola.justsimpleweather.api;


public interface ResultHandler<T> {
    void handle(T result);
    void reject(String reason);
}
