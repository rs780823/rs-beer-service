package com.rslowik.rsbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rslowik.rsbeerservice.web.dto.BeerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    private static String URI = "/api/v1/beer/";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(
                get(URI + UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());
    }

    @Test
    void createBeer() throws Exception {
        BeerDto dto = BeerDto.builder().build();
        String beerDtoJson = mapper.writeValueAsString(dto);

        mockMvc.perform(
                post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
        )
                .andExpect(status().isCreated());
    }

    @Test
    void updateBeer() throws Exception {
        BeerDto dto = BeerDto.builder().build();
        String beerDtoJson = mapper.writeValueAsString(dto);

        mockMvc.perform(
                put(URI + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
        )
                .andExpect(status().isNoContent());
    }
}