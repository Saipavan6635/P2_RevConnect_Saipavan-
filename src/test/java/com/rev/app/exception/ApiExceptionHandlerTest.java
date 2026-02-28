package com.rev.app.exception;

import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class ApiExceptionHandlerTest {

    @Test
    public void handleForbiddenBuildsExpectedBody() {
        ApiExceptionHandler handler = new ApiExceptionHandler();

        ResponseEntity<Map<String, Object>> response = handler
                .handleForbidden(new AccessDeniedException("Forbidden action"));

        assertEquals(403, response.getStatusCode().value());
        assertEquals("Forbidden", response.getBody().get("error"));
        assertEquals("Forbidden action", response.getBody().get("message"));
    }
}

