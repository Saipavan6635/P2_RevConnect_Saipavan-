package com.rev.app.config;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class WebSecurityConfigTest {

    @Test
    public void classShouldBeLoadable() {
        assertNotNull(WebSecurityConfig.class);
    }
}