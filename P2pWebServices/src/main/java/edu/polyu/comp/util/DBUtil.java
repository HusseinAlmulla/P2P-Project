package edu.polyu.comp.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class DBUtil {
	private static SessionFactory factory;

	public static SessionFactory getFactory() {
		if (factory == null) {
			factory = new Configuration().configure().buildSessionFactory();
		}
		return factory;
	}
}
