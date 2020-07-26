package ir.soroushtabesh.xo4.server.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBUtil {
    private static final Object lock = new Object();
    private static SessionFactory sessionFactory;
    private static Session session;

    private DBUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration()
                        .configure(Constants.HIBERNATE_CFG)
                        .setProperty("hibernate.connection.url", Constants.DB_URL_PREFIX + Constants.DB_URL)
                        .buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (session != null)
            session.close();
        sessionFactory.close();
    }

    private static Session getOpenSession() {
        if (session == null || !session.isOpen())
            session = getSessionFactory().openSession();
        return session;
    }

    private static void rawPush(Object object, Session session) {
        session.saveOrUpdate(object);
    }

    public static void pushSingleObject(Object object, Session session) {
        doInJPA(session, (session1 -> {
            rawPush(object, session1);
            return null;
        }));
    }

    public static void pushSingleObject(Object object) {
        pushSingleObject(object, getOpenSession());
    }

    public static void pushObjects(Object... args) {
        doInJPA((session) -> {
            for (Object arg : args) {
                rawPush(arg, session);
            }
            return null;
        });
    }

    public static void pushObjects(Session session, Object... args) {
        doInJPA(session, (session1) -> {
            for (Object arg : args) {
                rawPush(arg, session1);
            }
            return null;
        });
    }

    private static <T> T rawMerge(T object, Session session) {
        return (T) session.merge(object);
    }

    public static <T> T mergeSingleObject(T object, Session session) {
        return doInJPA(session, session1 -> rawMerge(object, session1));
    }

    public static <T> T mergeSingleObject(T object) {
        return mergeSingleObject(object, getOpenSession());
    }

    public static <T> T doInJPA(Transact<T> transact) {
        return doInJPA(getOpenSession(), transact);
    }

    public static <T> T doInJPA(Session session, Transact<T> transact) {
        T res;
        synchronized (lock) {
            boolean isActive = session.getTransaction().isActive();
            if (!isActive)
                session.beginTransaction();
            res = transact.transact(session);
            if (!isActive)
                session.getTransaction().commit();
        }
        return res;
    }

    public static <T> T doInJPATemp(Transact<T> transact) {
        T res;
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();
            res = transact.transact(session);
            session.getTransaction().commit();
        }
        return res;
    }

}
