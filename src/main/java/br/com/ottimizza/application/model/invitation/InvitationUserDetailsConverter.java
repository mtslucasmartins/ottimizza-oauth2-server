package br.com.ottimizza.application.model.invitation;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Converter(autoApply = true)
public class InvitationUserDetailsConverter implements AttributeConverter<InvitationUserDetails, String> {

    @Override
    public String convertToDatabaseColumn(InvitationUserDetails entityValue) {
        ObjectMapper mapper = new ObjectMapper();
        String userDetails = "{}";
        if (entityValue == null)
            return null;
        try {
            userDetails = mapper.writeValueAsString(entityValue);
        } catch (Exception e) {
            userDetails = "{}";
        }
        return userDetails;
    }

    @Override
    public InvitationUserDetails convertToEntityAttribute(String databaseValue) {
        ObjectMapper mapper = new ObjectMapper();
        InvitationUserDetails userDetails = null;
        if (databaseValue == null)
            return null;
        try {
            userDetails = mapper.readValue(databaseValue, InvitationUserDetails.class);
        } catch (Exception e) {
        }
        return userDetails;

    }
}