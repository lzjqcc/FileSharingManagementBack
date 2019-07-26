package com.Lowser.convert;

import com.Lowser.result.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;

public class ResponseMessageConverter extends MappingJackson2HttpMessageConverter {

    public ResponseMessageConverter(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) outputMessage;

        super.writeInternal(new ResponseEntity<>(o, httpResponse.getServletResponse().getStatus()), outputMessage);
    }

    @Override
    protected void writeInternal(Object object, @Nullable Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) outputMessage;
        super.writeInternal(new ResponseEntity<>(object, httpResponse.getServletResponse().getStatus()), type, outputMessage);


    }
}
