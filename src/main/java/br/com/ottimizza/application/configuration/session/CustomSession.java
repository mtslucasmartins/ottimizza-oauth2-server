package br.com.ottimizza.application.configuration.session;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.session.Session;

import lombok.Data;

public interface CustomSession extends Session {
    
    // private static final long serialVersionUID = 1L;

    // private String id;

    // private Instant creationTime;

    // private Instant lastAccessedTime;

    // private Boolean expired;

    // private SessionDetails details;

    // public CustomSession(Session session) {
    //     this.creationTime = session.getCreationTime();
    //     this.lastAccessedTime = session.getLastAccessedTime();
    //     this.expired = session.isExpired();

    //     this.details = session.getAttribute("SESSION_DETAILS");
    // }
    
    default SessionDetails getDetails() {
        return (SessionDetails) this.getAttribute("SESSION_DETAILS");
    }

}