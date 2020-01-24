package dao.generic;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionManager {

    private SessionManager() {
    }
    private static SessionFactory fact = null;

    public static Session getSession() {
        if (fact == null) {
            Configuration conf = new Configuration().configure();
            fact = conf.buildSessionFactory();
        }
        return fact.openSession();
    }

}
