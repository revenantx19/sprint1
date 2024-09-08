package com.skypro.sprint1.controllers;

import com.skypro.sprint1.controller.UserRecommendationController;
import com.skypro.sprint1.model.UserRecommendation;
import com.skypro.sprint1.service.UserRecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
public class UserRecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRecommendationService userRecommendationService;

    @InjectMocks
    private UserRecommendationController userRecommendationController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userRecommendationController).build();
    }

    @Test
    public void testGetRecommendations() throws Exception {
        UUID userId = UUID.randomUUID();
        List<UserRecommendation> recommendations = Arrays.asList(
                //new UserRecommendation(userId, "Recommendation 1"),
                //new UserRecommendation(userId, "Recommendation 2")
        );

        when(userRecommendationService.getRecommendations(userId)).thenReturn(recommendations);

        mockMvc.perform(get("/recommendation/{user_id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].userId").value(userId.toString()))
                .andExpect(jsonPath("$[0].recommendation").value("Recommendation 1"))
                .andExpect(jsonPath("$[1].userId").value(userId.toString()))
                .andExpect(jsonPath("$[1].recommendation").value("Recommendation 2"));

        verify(userRecommendationService, times(1)).getRecommendations(userId);
    }
}
