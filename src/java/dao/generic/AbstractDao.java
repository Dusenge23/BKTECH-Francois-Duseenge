package dao.generic;

import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractDao<K, T> implements IRootDao<K, T> {

    public static SessionFactory factory;
    // public Session session = null;
    //public Transaction t = null;
    protected Class<T> entityClass;
    private String CLASSNAME = "AbstractDao :: ";

    private static final transient Logger LOGGER = LoggerFactory
            .getLogger(AbstractDao.class);
    private static final int TRIM_CYCLE = 4;
    private static final String PERSISTANT_CLASS_IS_NULL = "Persistent Class is null";
    private static final String WHERE_CLAUSE = " where ";
    private static final String FROM_CLAUSE = " from ";
    private static final String WHERE_PROPERTY_HOLDER = "=?";

    @SuppressWarnings("unchecked")
    public AbstractDao() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.entityClass = (Class<T>) genericSuperclass
                .getActualTypeArguments()[1];

    }

    /* protected  SessionFactory hibernateFactory() {
     
     try {
       
        
     factory = new Configuration().configure("hibernate.cfg.xml")
     .addAnnotatedClass(entityClass).buildSessionFactory();
     System.out.println("Create Hibernate  sessionFactory  Started.. ");
     } catch (Throwable ex) {
     System.err.println("Failed to create Hibernate sessionFactory object" + ex);
     throw new ExceptionInInitializerError(ex);
     }
     return factory;
      
     }
    
     protected  Session getMyCurrentSession(){
     session.close();
     System.out.println("Create   Hibernate   session Started.. ");
     Session session = hibernateFactory().openSession();
      
     System.out.println("Hibernate session Created.. ");
     //hibernateFactory().close();
     //session.setFlushMode(FlushMode.AUTO);
     return session;
   
     }*/
    //  @SuppressWarnings("rawtypes")
    public List getGenericListWithHQLParameter(final String[] propertyName,
            final Object[] value, final String hqlStatement)
            throws Exception {
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        if (entityClass == null) {
            throw new Exception(
                    PERSISTANT_CLASS_IS_NULL);
        }
        try {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(FROM_CLAUSE);
            strQuery.append(hqlStatement);
            strQuery.append(WHERE_CLAUSE);

            for (int i = 0; i < propertyName.length; i++) {
                strQuery.append(propertyName[i]);
                strQuery.append(WHERE_PROPERTY_HOLDER + " and ");
            }

            strQuery.delete(strQuery.length() - TRIM_CYCLE,
                    strQuery.length() - 1);
            strQuery.append(") ");

            Query query = session.createQuery(
                    strQuery.toString());

            for (int i = 0; i < value.length; i++) {
                query.setParameter(i, value[i]);
            }

//            factory.close();
            List lis = query.list();
            session.close();
            return lis;
        } catch (HibernateException hibex) {

            LOGGER.error(hibex.getMessage(), hibex.getCause());
            throw new Exception(
                    hibex);

        } catch (Exception ex) {
            LOGGER.error("System Error has occured. {}", ex);
            throw new Exception(ex);
        } finally {

        }
    }
    
    public List getGenericListWithHQLParameterOr(final String[] propertyName,
            final Object[] value, final String hqlStatement)
            throws Exception {
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        if (entityClass == null) {
            throw new Exception(
                    PERSISTANT_CLASS_IS_NULL);
        }
        try {
            StringBuilder strQuery = new StringBuilder();
            strQuery.append(FROM_CLAUSE);
            strQuery.append(hqlStatement);
            strQuery.append(WHERE_CLAUSE);

            for (int i = 0; i < propertyName.length; i++) {
                strQuery.append(propertyName[i]);
                strQuery.append(WHERE_PROPERTY_HOLDER + " or ");
            }

            strQuery.delete(strQuery.length() - TRIM_CYCLE,
                    strQuery.length() - 1);
            strQuery.append(") ");

            Query query = session.createQuery(
                    strQuery.toString());

            for (int i = 0; i < value.length; i++) {
                query.setParameter(i, value[i]);
            }

            List lis = query.list();
            session.close();
            return lis;
        } catch (HibernateException hibex) {

            LOGGER.error(hibex.getMessage(), hibex.getCause());
            throw new Exception(
                    hibex);

        } catch (Exception ex) {
            LOGGER.error("System Error has occured. {}", ex);
            throw new Exception(ex);
        } finally {

        }
    }
    

    public List<T> getListByDateBewteenOtherCriteria(String dateColumn, Date startDate, Date endDate, final String[] propertyName, final Object[] value) throws ParseException {
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        try {

            Criteria cr = session.createCriteria(entityClass);
            cr.add(Restrictions.between(dateColumn, startDate, endDate));
            for (int i = 0; i < propertyName.length; i++) {
                for (int ii = 0; ii < value.length; ii++) {
                    cr.add(Restrictions.eq(propertyName[i], value[ii]));
                }
            }
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();
            return cr.list();
        } catch (Exception mm) {
            mm.printStackTrace();
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();
        } finally {

        }
        return null;
    }

    public List<T> getListModelWithMyHQL(final String[] propertyName, final Object[] value,
            final String hqlStatement, int maxResult) throws Exception {
        List<T> ls = new ArrayList<>();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        if (entityClass == null) {
            throw new Exception(
                    PERSISTANT_CLASS_IS_NULL);
        }
        try {

            StringBuilder strQuery = new StringBuilder();
            strQuery.append(hqlStatement);
            strQuery.append(WHERE_CLAUSE);

            for (int i = 0; i < propertyName.length; i++) {
                strQuery.append(propertyName[i]);
                strQuery.append(WHERE_PROPERTY_HOLDER + " and ");
            }

            strQuery.delete(strQuery.length() - TRIM_CYCLE,
                    strQuery.length() - 1);

            Query query = session.createQuery(
                    strQuery.toString());

            for (int i = 0; i < value.length; i++) {
                query.setParameter(i, value[i]);
            }
            if (maxResult > 0) {
                query.setMaxResults(maxResult);
            }
            ls = query.list();
            return ls;
        } catch (HibernateException hibex) {
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
            LOGGER.error(hibex.getMessage(), hibex);
            throw new Exception(hibex);
        } catch (Exception ex) {
            LOGGER.error("System Error has occured. {}", ex);
            throw new Exception(ex);
        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
        }
    }

    public T getModelWithMyHQL(final String[] propertyName, final Object[] value,
            final String hqlStatement) throws Exception {
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        if (entityClass == null) {
            throw new Exception(
                    PERSISTANT_CLASS_IS_NULL);
        }
        try {

            StringBuilder strQuery = new StringBuilder();
            strQuery.append(hqlStatement);
            strQuery.append(WHERE_CLAUSE);

            for (int i = 0; i < propertyName.length; i++) {
                strQuery.append(propertyName[i]);
                strQuery.append(WHERE_PROPERTY_HOLDER + " and ");
            }

            strQuery.delete(strQuery.length() - TRIM_CYCLE,
                    strQuery.length() - 1);

            Query query = session.createQuery(
                    strQuery.toString());

            for (int i = 0; i < value.length; i++) {
                query.setParameter(i, value[i]);
            }

            return (T) query.uniqueResult();
        } catch (HibernateException hibex) {
            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();
            LOGGER.error(hibex.getMessage(), hibex);
            throw new Exception(hibex);
        } catch (Exception ex) {
            LOGGER.error("System Error has occured. {}", ex);
            throw new Exception(ex);
        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();

        }
    }
    
        public T getModelWithMyHQLOr(final String[] propertyName, final Object[] value,
            final String hqlStatement) throws Exception {
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        if (entityClass == null) {
            throw new Exception(
                    PERSISTANT_CLASS_IS_NULL);
        }
        try {

            StringBuilder strQuery = new StringBuilder();
            strQuery.append(hqlStatement);
            strQuery.append(WHERE_CLAUSE);

            for (int i = 0; i < propertyName.length; i++) {
                strQuery.append(propertyName[i]);
                strQuery.append(WHERE_PROPERTY_HOLDER + " or ");
            }

            strQuery.delete(strQuery.length() - TRIM_CYCLE,
                    strQuery.length() - 1);

            Query query = session.createQuery(
                    strQuery.toString());

            for (int i = 0; i < value.length; i++) {
                query.setParameter(i, value[i]);
            }

            return (T) query.uniqueResult();
        } catch (HibernateException hibex) {
            if (!t.wasCommitted()) {
                t.commit();
            }
            
            LOGGER.error(hibex.getMessage(), hibex);
            throw new Exception(hibex);
        } catch (Exception ex) {
            LOGGER.error("System Error has occured. {}", ex);
            throw new Exception(ex);
        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();

        }
    }
    
    

    public List<T> getModelWithGroupBy(final String[] propertyName, final Object[] value,
            final String hqlStatement, final String groupByColumn) {
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        StringBuilder stringBuilderWorld = new StringBuilder();
        Criteria cr = session.createCriteria(entityClass);
        for (int i = 0; i < propertyName.length; i++) {
            cr.add(Restrictions.eq(propertyName[i], value[i]));
        }
        ProjectionList projectionList = Projections.projectionList();
        projectionList.add(Projections.groupProperty(groupByColumn));
        cr.setProjection(projectionList);
        List<T> list = cr.list();
        session.close();
        return list;
    }

    public List<T> getAllWithGroupBy(String value, String[] listOfProperty) throws Exception {
        Session session = SessionManager.getSession();
        List<T> list = new ArrayList<>();
        try {
            Criteria cr = session.createCriteria(entityClass);
            ProjectionList projections = Projections.projectionList();
            projections.add(Projections.groupProperty(value), value);
            for (String x : listOfProperty) {
                projections.add(Projections.property(x).as(x));
            }
            cr.setProjection(projections);
            cr.setResultTransformer(Transformers.aliasToBean(entityClass));
            list = cr.list();
        } catch (Exception e) {
            LOGGER.error("System Error has occured.{}", e);
            throw new Exception(e);
        } finally {
            session.close();
        }

        return list;
    }

    public T saveIntable(T model) {

        LOGGER.info(CLASSNAME + "SAVE start");
        System.out.println("Transaction Started.. for save config");
//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();

        try {

            session.save(model);
            t.commit();

            LOGGER.info(CLASSNAME + "SAVE end");
            return model;
        } catch (Exception k) {
            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();

            k.printStackTrace();
            if (t != null) {
                t.rollback();
            }
        } finally {
            if (!t.wasCommitted()) {
                t.commit();
            }

            session.close();
//            factory.close();

        }

        return model;

    }

    public long getLastRecordId(String sql) {
        //final String sql = "SELECT max( i.id ) FROM Item i";
//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        try {

            return (Integer) session.createQuery(sql).uniqueResult();
        } catch (Exception k) {
        } finally {
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();
        }
        return 0;
    }

    public T updateIntable(T model) {
//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        try {
            System.out.println("Transaction Started.. for update config");
            session.update(model);
            t.commit();
            session.close();
            System.out.println("Here we are boss wanjye ");
            return model;

        } catch (Exception j) {
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();
            j.printStackTrace();
        }

        return model;
    }

    public Object getLongIn(String userName, String password) {

        Criteria cr;
//        factory = new AnnotationConfiguration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass.getClass()).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();

        Transaction t = session.beginTransaction();

        Object val = new Object();

        try {

            cr = session.createCriteria(entityClass.getClass());
            cr.add(Restrictions.eq("password", password));
            cr.add(Restrictions.eq("userName", userName));
            List a = cr.list();
            System.out.println(a.size());
            System.out.println("Here we are!!!" + session.isOpen());
            System.out.println("Here we are!!!" + factory.isClosed());
            for (Iterator iterator = a.iterator(); iterator.hasNext();) {
                val = (Class<T>) iterator.next();

            }
            System.out.println("Here we are2!!!" + session.isOpen());
            System.out.println("Here we are2!!!" + factory.isClosed());
            return val;
        } catch (Exception jj) {
            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();
            session.close();
            System.out.println("IN check paasword METHOD ERROR ");
            System.out.println(jj.getMessage());
            jj.printStackTrace();

        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();
            return val;
        }

    }

    public List<T> getListByDateBewteen(String dateColumn, Date startDate, Date endDate) throws ParseException {
        // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//Date fromDate = df.parse(startDate);
//Date toDate = df.parse(endDate);
//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();

        try {

            Criteria criteria = session.createCriteria(entityClass)
                    .add(Restrictions.between(dateColumn, startDate, endDate));
            return criteria.list();
        } catch (Exception e) {
            e.printStackTrace();
            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();
            session.close();

        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();
            session.close();

        }
        return null;
    }

    public boolean creatingNewTable() {
        //  Session session = getMyCurrentSession();

        return true;
    }

    public List<Object> getModelList() {

//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        List ite = null;

        try {
            //  t = session.beginTransaction();
            ite = session.createQuery("FROM " + entityClass.getName()).list();

        } catch (HibernateException e) {

            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();
            session.close();

            e.printStackTrace();
        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
//            factory.close();
            session.close();

        }
        return ite;

    }

    public Object getModelById(int modelId, String PrimaryKeycolumnName) {
        Criteria cr;
        Object ob = null;
//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        LOGGER.info("here i am in getModelById!!");

        try {

            cr = session.createCriteria(entityClass.getName());
            cr.add(Restrictions.eq(PrimaryKeycolumnName, modelId));

            List a = cr.list();
            for (Iterator iterator = a.iterator(); iterator.hasNext();) {
                ob = iterator.next();
                System.out.println("here i am!! test");
                LOGGER.info("here in for loop!!");
            }

        } catch (Exception jj) {
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();

            jj.printStackTrace();
//t.rollback();
            if (t != null) {
                t.rollback();
            }
        } finally {

            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();

        }

        return ob;
    }

    public Object getLongInUserDeails(String userName) {

        Criteria cr;
        Session session = SessionManager.getSession();
        Transaction t = session.beginTransaction();
        Object val = new Object();

        try {
            cr = session.createCriteria(entityClass);
            cr.add(Restrictions.eq("userName", userName));
            List a = cr.list();
            for (Iterator iterator = a.iterator(); iterator.hasNext();) {
                val = (Class<T>) iterator.next();
            }

        } catch (Exception jj) {
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
            jj.printStackTrace();
        } finally {
            if (!t.wasCommitted()) {
                t.commit();
            }
            session.close();
//            factory.close();
        }

        return val;
    }

//    public List<User> getUserListByCat(UserCategory category) {
//
//        Criteria cr;
//        List a = null;
//        factory = new Configuration().configure("hibernate.cfg.xml")
//                .addAnnotatedClass(entityClass).buildSessionFactory();
//        Session session = factory.openSession();
//        Transaction t = session.beginTransaction();
//
//        try {
//            // t= session.beginTransaction();
//            cr = session.createCriteria(entityClass);
//
//            cr.add(Restrictions.eq("userCategory", category));
//
//            a = cr.list();
//
//        } catch (Exception jj) {
//            if (!t.wasCommitted()) {
//                t.commit();
//            }
//            session.close();
//            factory.close();
//
//            jj.printStackTrace();
//        } finally {
//
//            if (!t.wasCommitted()) {
//                t.commit();
//            }
//            session.close();
//            factory.close();
//        }
//
//        return a;
//    }
    @Override
    public void persist(T entity) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T findById(K id) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> getEntitiesWithPagination() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T getModel(K modelId) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T insert(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T update(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> getList(int startRecord, int numberOfRecord) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(T model) throws Exception {
        try {
            Session session = SessionManager.getSession();
            Transaction ts = session.beginTransaction();
            session.delete(model);
            ts.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public int getNumberOfRecords(T model) throws Exception {

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T saveOrUpdate(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T saveOnFlush(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T merge(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T mergeT(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public T refresh(T model) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<T> search(String value, String columname, Class className) {

        Session session = SessionManager.getSession();
        Criteria cr = session.createCriteria(className);
        cr.add(Restrictions.like(columname, value, MatchMode.START));
        List<T> list = cr.list();
        session.close();
        return list;

    }

    /**
     *
     * @return
     */
}
