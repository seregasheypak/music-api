package com.amonge.sandbox.musicartisapi.api;

import com.amonge.sandbox.musicartisapi.pojo.Artist;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ArtistControllerIT {
    private MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private ArtistController artistController;

    @Before
    public void setup() throws Exception {
        this.mockMvc = standaloneSetup(this.artistController).build();
    }

    @Test
    public void testSearchASync() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/v1/artist/cc197bad-dc9c-440d-a5b5-d52ba2e14234").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(request().asyncStarted())
                .andDo(print())
                .andReturn();

        Artist response = (Artist) mockMvc.perform(asyncDispatch(result))
                .andDo(print())
                .andExpect(status().isOk()).andReturn().getAsyncResult();


        assertEquals("mbid property is returned", "cc197bad-dc9c-440d-a5b5-d52ba2e14234", response.getMbid());
    }
}
