package ir.soroushtabesh.xo4.server.utils;

import org.hibernate.Session;

public interface Transact<T> {
    T transact(Session session);
}
