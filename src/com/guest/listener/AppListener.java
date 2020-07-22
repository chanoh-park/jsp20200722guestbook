package com.guest.listener;

import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.dbcp2.ConnectionFactory;
import org.apache.commons.dbcp2.DriverManagerConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDriver;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Application Lifecycle Listener implementation class AppListener
 *
 */
@WebListener
public class AppListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public AppListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce)  { 
    	// 1. context path -> application attribute에 넣기
    	addContextPath(sce);
    	
    	// 2. create connection pool
    	createConnectionPool();
    }

	private void createConnectionPool() {
		try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		String jdbcUrl = "jdbc:mysql://localhost/guestbook" + "?serverTimezone=Asia/Seoul";
    		String usename = "root";
    		String pw = "rootpw";
    		
    		ConnectionFactory connFactory = new DriverManagerConnectionFactory(jdbcUrl, usename, pw);
    		
    		PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
    		poolableConnFactory.setValidationQuery("select 1");
    		
    		GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
    		poolConfig.setTimeBetweenEvictionRunsMillis(1000 * 60L * 5L);
    		poolConfig.setTestWhileIdle(true);
    		poolConfig.setMinIdle(4);
    		poolConfig.setMaxTotal(50);
    		
    		GenericObjectPool<PoolableConnection> connectionPool = new GenericObjectPool<>(poolableConnFactory, poolConfig);
    		poolableConnFactory.setPool(connectionPool);
    		
    		Class.forName("org.apache.commons.dbcp2.PoolingDriver");
    		PoolingDriver driver = (PoolingDriver) DriverManager.getDriver("jdbc:apache:commons:dbcp:");
    		driver.registerPool("guestbook", connectionPool);
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
	}

	private void addContextPath(ServletContextEvent sce) {
		ServletContext app = sce.getServletContext();
		String rootPath = app.getContextPath();
		
		app.setAttribute("rootPath", rootPath);
	}
    
    
	
}
