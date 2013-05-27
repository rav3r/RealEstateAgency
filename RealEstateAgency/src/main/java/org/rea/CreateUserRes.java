package org.rea;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(namespace = "http://rea.org/soa/types/",
		 name = "CreateUserRes")
public enum CreateUserRes {
        OK,
        INVALID_PASSWORD,
        INVALID_USER 
}

///////////////////////////////////////////////////////////////////////////////
// Sciagawka - przykladowa klasa ktora mozna zwracac w WebMetodzie
//
/*package soa.examples.model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement
@XmlType(namespace = "http://ex.agh.edu.pl/soa/types/",
		 name = "AuthorizeResponse",
		 propOrder = { "logged", "message", "sessionId" }) // TUTAJ TRZEBA DOPISAC SWOJE ZMIENNE
public class AuthorizeResponse {
	private boolean logged; // TE ZMIENNE MUSZA MIEC GETTERY I SETTERY
	private String message;
	private String sessionId;

	public AuthorizeResponse() {
		super();
	}

	public boolean isLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}*/
///////////////////////////////////////////////////////////////////////////////