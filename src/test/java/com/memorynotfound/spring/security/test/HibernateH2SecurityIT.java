package com.memorynotfound.spring.security.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("it")
@AutoConfigureMockMvc
@RunWith(SpringJUnit4ClassRunner.class)
public class HibernateH2SecurityIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void accessWithValidCredentials() throws Exception {
        this.mockMvc
                .perform(get("/users").with(httpBasic("info@memorynotfound.com", "password")))
                .andExpect(status().isOk());
    }

    @Test
    public void accessWithInValidCredentials() throws Exception {
        this.mockMvc
                .perform(get("/users").with(httpBasic("info@memorynotfound.com", "invalidPassword")))
                .andExpect(status().is4xxClientError());
    }

}
