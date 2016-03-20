package edu.polyu.comp.dao;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;

import edu.polyu.comp.constants.UserConstants;
import edu.polyu.comp.domain.User;
import edu.polyu.comp.util.DBUtil;
import edu.polyu.comp.util.LoggerUtil;
import edu.polyu.comp.util.StringUtil;

public class UserDao {
	
	public boolean createUser(User user) {
		boolean isSuccess = true;
		Session session = DBUtil.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			isSuccess = false;
		} finally {
			session.disconnect();
		}
		return isSuccess;
	}
	
	public boolean updateUser(User user) {
        boolean isSuccess = true;
        Session session = DBUtil.getFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
			isSuccess = false;
            e.printStackTrace();
        } finally {
        	session.disconnect();
        }
        return isSuccess;
    }
	
	@SuppressWarnings("unchecked")
	public User findUserByPhoneNumber(String phoneNumber) {
		User user = null;
		if (!StringUtil.isEmpty(phoneNumber)) {
			Session session = DBUtil.getFactory().openSession();
			FullTextSession fullTextSession = Search.getFullTextSession(session);
			try {				
				fullTextSession.createIndexer();				
				StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);
				BooleanQuery finalQuery = new BooleanQuery();
				QueryParser queryParser = new QueryParser(Version.LUCENE_35, UserConstants.DOMAIN_NAME_PHONE, analyzer);
				Query query = queryParser.parse(phoneNumber);
				finalQuery.add(query, Occur.MUST);
				org.hibernate.Query hibQuery = fullTextSession.createFullTextQuery(finalQuery);
				List<User> result = hibQuery.list();
				if (result != null && result.size() > 0) {
					user = result.get(0);
				} else {
					LoggerUtil.info(this.getClass().getName(), "No user with phone number = " + phoneNumber + " can be found.");
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				fullTextSession.disconnect();
				session.disconnect();
			}
		}
		return user;
	}
}
