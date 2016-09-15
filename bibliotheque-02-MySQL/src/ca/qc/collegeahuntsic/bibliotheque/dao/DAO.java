
package ca.qc.collegeahuntsic.bibliotheque.dao;

import java.io.Serializable;

import com.mysql.jdbc.Connection;

import ca.qc.collegeahuntsic.bibliotheque.db.Connexion;

public class DAO implements Serializable {
	


	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	public DAO(Connexion connexion) {
		// TODO Auto-generated constructor stub
		super();
		//setConnexion(connexion);
	}
	/*
	protected Connection getConnection(){
		return getConnexion().getConnection();
	}*/
}
