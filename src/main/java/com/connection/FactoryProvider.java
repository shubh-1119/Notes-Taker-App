package com.connection;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.entities.Notes;

import java.util.Properties;

public class FactoryProvider {
    private static SessionFactory factory;

    public static SessionFactory getFactory() {
        if (factory == null) {
            try {
                // Load DB connection details from environment
                String dbUrl = System.getenv("JDBC_DATABASE_URL");
                String dbUser = System.getenv("JDBC_DATABASE_USERNAME");
                String dbPass = System.getenv("JDBC_DATABASE_PASSWORD");

                Properties props = new Properties();
                props.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");

                if (dbUrl != null && !dbUrl.isEmpty() &&
                    dbUser != null && !dbUser.isEmpty() &&
                    dbPass != null && !dbPass.isEmpty()) {
                    // Use environment variables if they are set
                    props.setProperty("hibernate.connection.url", dbUrl);
                    props.setProperty("hibernate.connection.username", dbUser);
                    props.setProperty("hibernate.connection.password", dbPass);
                    System.out.println("Using environment variables for DB connection.");
                } else {
                    // Use local DB details if environment variables are not set
                    System.out.println("Environment variables not found. Using local DB details.");
                    props.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/notestaker"); 
                    props.setProperty("hibernate.connection.username", "root"); 
                    props.setProperty("hibernate.connection.password", "Shubh@2410");
                }

                props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
                props.setProperty("hibernate.hbm2ddl.auto", "update");
                props.setProperty("hibernate.show_sql", "true");
                props.setProperty("hibernate.format_sql", "true");

                factory = new Configuration()
                        .addProperties(props)
                        .addAnnotatedClass(Notes.class)
                        .buildSessionFactory();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return factory;
    }

    public static void closeFactory() {
        if (factory != null) {
            factory.close();
        }
    }
}