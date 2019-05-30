package com.app.main;

import org.springframework.boot.builder.SpringApplicationBuilder;

public class ServletInitializer extends org.springframework.boot.web.servlet.support.SpringBootServletInitializer{
    
	@Override
    protected SpringApplicationBuilder configure(
      SpringApplicationBuilder builder) {
        return builder.sources(BookswapApplication.class);
    }

}