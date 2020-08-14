package com.db;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.File;

public class SessionFactoryUtil {
	private volatile static SessionFactoryUtil m_UtilInstance = null;
	private static SessionFactory m_SessionFactory= null;
  	public final String CONFIG_FILE = "hibernate2.cfg.xml";
	private SessionFactoryUtil(){
		try{
			String rcPath = System.getProperty("user.dir") + "/conf";
			String path = String.format("%1$s/%2$s", rcPath, CONFIG_FILE);
			File file = new File(path);
			StandardServiceRegistry  serviceRegistry = null;
			if(file.exists()){
         		serviceRegistry=new StandardServiceRegistryBuilder().configure(file).build();  
          	}else
          		serviceRegistry=new StandardServiceRegistryBuilder().configure().build();  
      		m_SessionFactory=new MetadataSources(serviceRegistry).buildMetadata().buildSessionFactory();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static SessionFactoryUtil getInstance(){
		if(m_UtilInstance == null){
			synchronized(SessionFactoryUtil.class){
				if(m_SessionFactory == null){
					m_UtilInstance = new SessionFactoryUtil();
				}
			}
		}
		return m_UtilInstance;
	}
	
	public SessionFactory getSessionFactory(){
		return m_SessionFactory;
	}
	
	public static void closeSessionFactory(){
		if(m_SessionFactory != null){
			m_SessionFactory.close();
			m_SessionFactory = null;
			m_UtilInstance = null;
		}
	}
}
