package br.com.ottimizza.application.configuration.session;

import java.io.Serializable;

import lombok.Data;

@Data
public class SessionDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String location;

    private String accessType;

}
