package main.java.selmamalic.com.todo.Utilities;

import main.java.selmamalic.com.todo.modules.Tasks;
import main.java.selmamalic.com.todo.modules.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory;
    private static ServiceRegistry serviceRegistry;
    public static SessionFactory createSessionFactory() {
        Configuration configuration = new Configuration().configure("main/resources/hibernate.cfg.xml");
        configuration.addAnnotatedClass(User.class);
        configuration.addAnnotatedClass(Tasks.class);
        serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                configuration.getProperties()).build();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        return sessionFactory;
    }

    public static void close() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }
}
