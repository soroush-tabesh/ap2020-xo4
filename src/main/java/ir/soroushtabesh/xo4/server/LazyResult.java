package ir.soroushtabesh.xo4.server;

public interface LazyResult<T> {
    void call(T result);
}
