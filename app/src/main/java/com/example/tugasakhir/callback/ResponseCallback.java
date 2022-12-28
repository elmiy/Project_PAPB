package com.example.tugasakhir.callback;

public abstract class ResponseCallback<T> {
    public abstract void onSuccess(T data);
    public abstract void onError(String message);

    public void onLoading() {}
    public void onEmpty() {}
}
