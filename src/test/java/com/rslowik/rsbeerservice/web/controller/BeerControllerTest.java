package com.rslowik.rsbeerservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rslowik.rsbeerservice.services.BeerService;
import com.rslowik.rsbeerservice.web.dto.BeerDto;
import com.rslowik.rsbeerservice.web.dto.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(RestDocumentationExtension.class)
@AutoConfigureRestDocs
@WebMvcTest(BeerController.class)
@ComponentScan(basePackages = "com.rslowik.rsbeerservice.web.mappers")
class BeerControllerTest {

    private static String URI = "/api/v1/beer/";

    @MockBean
    private BeerService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getBeerById() throws Exception {
        ConstrainedFields fields = new ConstrainedFields(BeerDto.class);

        given(service.getBeerById(any(UUID.class))).willReturn(BeerDto.builder().build());

        mockMvc.perform(
                get(URI + "{beerId}", UUID.randomUUID().toString())
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(
                        document("v1/beer-get",
                                pathParameters(
                                        parameterWithName("beerId").description("UUID of desired beer to get")
                                ),
                                responseFields(
                                        fields.withPath("id").description("ID of the Beer").type(UUID.class),
                                        fields.withPath("createdDate").description("Date Created").type(OffsetDateTime.class),
                                        fields.withPath("lastModifiedDate").description("Date Modified").type(OffsetDateTime.class),
                                        fields.withPath("beerName").description("Name of the Beer").type(String.class),
                                        fields.withPath("upc").description("UPC").type(Long.class),
                                        fields.withPath("beerStyle").description("Style of the Beer"),
                                        fields.withPath("version").description("Version of the Beer"),
                                        fields.withPath("price").description("Price of the Beer"),
                                        fields.withPath("quantityOnHand").description("Quantity on hand")
                                )
                        )
                );
    }

    @Test
    void createBeer() throws Exception {
        String beerDtoJson = mapper.writeValueAsString(getValidBeerDto());
        given(service.saveNewBeer(any(BeerDto.class))).willReturn(BeerDto.builder().build());

        mockMvc.perform(
                post(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
        )
                .andExpect(status().isCreated());
    }

    @Test
    void updateBeer() throws Exception {
        String beerDtoJson = mapper.writeValueAsString(getValidBeerDto());
        mockMvc.perform(
                put(URI + UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(beerDtoJson)
        )
                .andExpect(status().isNoContent());
    }

    private BeerDto getValidBeerDto() {


        return BeerDto.builder()
                .beerName("NewAle")
                .beerStyle(BeerStyleEnum.ALE)
                .upc("0083783375213")
                .price(new BigDecimal("12.45"))
                .build();
    }

    private static class ConstrainedFields {
        private final ConstraintDescriptions constraintDescriptions;


        private ConstrainedFields(Class<?> input) {
            this.constraintDescriptions = new ConstraintDescriptions(input);
        }

        private FieldDescriptor withPath(String path) {
            return fieldWithPath(path).attributes(key("constraints")
                    .value(StringUtils.collectionToDelimitedString(
                            this.constraintDescriptions.descriptionsForProperty(path), ", ")
                    )
            );
        }
    }
}