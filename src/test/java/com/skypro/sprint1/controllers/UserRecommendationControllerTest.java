package com.skypro.sprint1.controllers;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


/*
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

 */
