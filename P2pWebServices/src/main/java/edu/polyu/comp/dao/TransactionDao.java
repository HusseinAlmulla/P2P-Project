package edu.polyu.comp.dao;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import edu.polyu.comp.util.DBUtil;

public class TransactionDao {
	
	public boolean createTransaction(edu.polyu.comp.domain.Transaction transaction) {
		boolean isSuccess = true;
		Session session = DBUtil.getFactory().openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(transaction);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			isSuccess = false;
		} finally {
			session.disconnect();
			/* End the session by releasing the JDBC connection and cleaning up.  
			 * It is not strictly necessary to close the session but you must at least
			 */
			session.close();
		}
		return isSuccess;
	}
}
