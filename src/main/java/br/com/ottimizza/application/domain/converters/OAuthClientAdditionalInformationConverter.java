package br.com.ottimizza.application.domain.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ottimizza.application.model.OAuthClientAdditionalInformation;

@Converter(autoApply = true)
public class OAuthClientAdditionalInformationConverter
        implements AttributeConverter<OAuthClientAdditionalInformation, String> {

    @Override
    public String convertToDatabaseColumn(OAuthClientAdditionalInformation entityValue) {
        ObjectMapper mapper = new ObjectMapper();
        String additionalInformation = null;
        if (entityValue == null)
            return null;
        try {
            additionalInformation = mapper.writeValueAsString(entityValue);
        } catch (Exception e) {
        }
        return additionalInformation;
    }

    @Override
    public OAuthClientAdditionalInformation convertToEntityAttribute(String databaseValue) {
        ObjectMapper mapper = new ObjectMapper();
        OAuthClientAdditionalInformation additionalInformation = null;
        if (databaseValue == null)
            return null;
        try {
            additionalInformation = mapper.readValue(databaseValue, OAuthClientAdditionalInformation.class);
        } catch (Exception e) {
        }
        return additionalInformation;

    }
}