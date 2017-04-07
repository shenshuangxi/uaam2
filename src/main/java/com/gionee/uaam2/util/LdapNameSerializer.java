package com.gionee.uaam2.util;

import java.io.IOException;

import javax.naming.ldap.LdapName;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LdapNameSerializer extends JsonSerializer<LdapName> {

	@Override
	public void serialize(LdapName value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
		gen.writeString(value.toString());
	}
}