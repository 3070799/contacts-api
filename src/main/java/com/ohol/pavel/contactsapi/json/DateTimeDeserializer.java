package com.ohol.pavel.contactsapi.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;

/**
 * Implements custom Jackson deserialization.
 */
public final class DateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

    @Override
    public OffsetDateTime deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        return OffsetDateTime.parse(p.getValueAsString());
    }
}
