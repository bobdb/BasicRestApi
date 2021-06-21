package com.example.basicrestapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserServiceTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private static final User TEST_USER = new User();
    private static final List<Program> TEST_PROGRAMS = new ArrayList<>();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final int DEFAULT_POINTS = 999;

    @BeforeClass
    public static void beforeClass() throws Exception {
        // copy of test user in configuration class
        TEST_USER.setName("Johnny TestUser");
        TEST_USER.setPoints(DEFAULT_POINTS);

        // copy of hardcoded rewards programs
        TEST_PROGRAMS.add(new Program("Company1", 2.0));
        TEST_PROGRAMS.add(new Program("Company2", 3.0));
        TEST_PROGRAMS.add(new Program("Company3", 0.5));
    }

    @Before
    public void setUp() throws Exception {
        // Set set test user (id=1) to default state and clear entries from repository
        userRepository.updatePointsById(1,DEFAULT_POINTS);
        mvc.perform(MockMvcRequestBuilders.get("/user/1/reset"));
    }

    @Test
    public void checkTestUserPresentFromApplicationBootstrap() throws Exception {
        // Given the predefined TEST_USER

        // When we request the user
        User actual = userRepository.findById(1);

        // Then the endpoint returns successfully
        assertEquals(TEST_USER.getName(), actual.getName());
        assertEquals(TEST_USER.getPoints(), actual.getPoints());
    }

    @Test
    public void checkUsersEndpoint() throws Exception {
        // Given default setup

        // When /users endpoint receives GET

        // Then an ok response is received with a length 1 list of users,
        // containing the test user
        mvc.perform(MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((ResultMatcher) jsonPath("$[0].name", Matchers.is(TEST_USER.getName())))
                .andExpect((ResultMatcher) jsonPath("$[0].points", Matchers.is((int)TEST_USER.getPoints())));
    }

    @Test
    public void checkTestUser() throws Exception {
        // Given default setup

        // When the test user resource is requested
        // Then the GET is performed successfully returning expected results
        mvc.perform(MockMvcRequestBuilders.get("/user/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", Matchers.is(TEST_USER.getName())))
                .andExpect((ResultMatcher) jsonPath("$.points", Matchers.is((int)TEST_USER.getPoints())));
    }

    @Test
    public void programsEndpointShouldReturnTestCompanies() throws Exception {
        // Given no entries in PointTransfer repository

        // When the programs are are requested for a user
        // Then the list of Program objects is returned successfully, populated with expected values
		mvc.perform(MockMvcRequestBuilders.get("/user/1/programs")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$[0].name", Matchers.is(TEST_PROGRAMS.get(0).getName())))
                .andExpect((ResultMatcher) jsonPath("$[0].exchangeRate", Matchers.is(TEST_PROGRAMS.get(0).getExchangeRate())))
                .andExpect((ResultMatcher) jsonPath("$[1].name", Matchers.is(TEST_PROGRAMS.get(1).getName())))
                .andExpect((ResultMatcher) jsonPath("$[1].exchangeRate", Matchers.is(TEST_PROGRAMS.get(1).getExchangeRate())))
                .andExpect((ResultMatcher) jsonPath("$[2].name", Matchers.is(TEST_PROGRAMS.get(2).getName())))
                .andExpect((ResultMatcher) jsonPath("$[2].exchangeRate", Matchers.is(TEST_PROGRAMS.get(2).getExchangeRate())));

    }


   @Test
    public void fundWithValidAmountIsOk() throws Exception {
       // Given no entries in PointTransfer repository
       // And a valid funding request
        FundingRequest request = new FundingRequest();
        request.setAmount(123);

        // When the funding is requested
        // Then the funding is successful
        mvc.perform(post("/user/1/fund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("SUCCESS")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is((int)request.getAmount())))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS+(int)request.getAmount())));
            // NOTE:  not checking the message field, it's just pretty text
    }
    @Test
    public void fundWithNegativeOrZeroFails() throws Exception {
        // Given no entries in PointTransfer repository
        // And a funding request is for a negative value
        FundingRequest request = new FundingRequest();
        request.setAmount(-1);

        // When the funding is requested
        // Then it fails
        mvc.perform(post("/user/1/fund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
					.andExpect(status().isBadRequest())
					.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
					.andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
					.andExpect((ResultMatcher) jsonPath("$.message", Matchers.is("Bad funding")))
					.andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(-1)))
					.andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
					.andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)));

        // When a funding request is for zero
        request.setAmount(0);
        // Then it fails
        mvc.perform(post("/user/1/fund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
                .andExpect((ResultMatcher) jsonPath("$.message", Matchers.is("Bad funding")))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(0)))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)));
    }

    @Test
    public void transferWithValidRequestSucceeds() throws Exception {
        // Given no entries in PointTransfer repository
        // And a valid transfer request
        Pointtransfer request = new Pointtransfer();
        request.setAmount(100);
        request.setDestination("Company1");

        // When the transfer is requested
        // Then it succeeds
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("SUCCESS")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(100)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(899)))
                .andExpect((ResultMatcher) jsonPath("$.destination", Matchers.is("Company1")));

    }

    @Test
    public void transferWithNegativeOrZeroFails() throws Exception {
        // Given no entries in PointTransfer repository
        // And a transfer request for a negative number
        Pointtransfer request = new Pointtransfer();
        request.setAmount(-1);
        request.setDestination("Company1");

        // When the transfer is requested
        // It fails
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(-1)));


        //userRepository.updatePointsById(1,DEFAULT_POINTS);

        // When a transfer is requested for a value of zero
        request.setAmount(0);
        // Then it fails
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(0)));

    }

    @Test
    public void transferWhenExchangeRateCausesLessThanOnePointFails() throws Exception {
        // Given no entries in PointTransfer repository
        // And a transfer request when the exchange rate invalidates the request amount
        Pointtransfer request = new Pointtransfer();
        request.setAmount(1);
        request.setDestination("Company3");

        // When a transfer is requested
        // Then it fails
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(1)));

    }

    @Test
    public void transferWhenNotSubscribedToProgramFails() throws Exception {
        // Given no entries in PointTransfer repository
        // And a transfer request when the user is not subscribed to rewards program
        Pointtransfer request = new Pointtransfer();
        request.setAmount(1);
        request.setDestination("NotACompany");

        // When a transfer is requested
        // Then it fails
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(1)));
    }

    @Test
    public void transferWhenBalanceIsTooLowFails() throws Exception {
        // Given no entries in PointTransfer repository
        // And a transfer request that exceeds the current amount balance
        Pointtransfer request = new Pointtransfer();
        request.setAmount(100000);
        request.setDestination("Company1");

        // When a transfer is requested
        // Then it fails
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect((ResultMatcher) jsonPath("$.status", Matchers.is("FAILURE")))
                .andExpect((ResultMatcher) jsonPath("$.startingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.endingBalance", Matchers.is(DEFAULT_POINTS)))
                .andExpect((ResultMatcher) jsonPath("$.amount", Matchers.is(100000)));
    }

    @Test
    public void HistoryReturnsSuccessfullyWhetherEmptyOrPopulated() throws Exception {
        // Given no entries in PointTransfer repository

        // When the history is requested
        // Then the response has length zero, and is also 200 (OK)
        mvc.perform(MockMvcRequestBuilders.get("/user/1/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        // Given no entries in PointTransfer repository
        // When a have a successful funding
        FundingRequest request = new FundingRequest();
        request.setAmount(123);
        mvc.perform(post("/user/1/fund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // Then a record of it is made in the repository
        // And When the history is requested
        // Then the response has length zero, and is also 200 (OK)
        mvc.perform(MockMvcRequestBuilders.get("/user/1/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }

    @Test
    public void successfulFundingIsPersisted() throws Exception {
        // Given no entries in PointTransfer repository
        // And a valid funding request
        FundingRequest request = new FundingRequest();
        request.setAmount(123);

        // When the funding is requested
        mvc.perform(post("/user/1/fund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Then it is persisted
        mvc.perform(MockMvcRequestBuilders.get("/user/1/history")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect((ResultMatcher)jsonPath("$[0].status", Matchers.is("SUCCESS")));
    }

    @Test
    public void unsuccessfulFundingIsNotPersisted() throws Exception {
        // Given no entries in PointTransfer repository
        // And a funding request with an invalid value
        FundingRequest request = new FundingRequest();
        request.setAmount(-123);

        // When the funding request is made
        // Then it returns as a Bad Request
        mvc.perform(post("/user/1/fund")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // And
        // Then it is persisted
 		mvc.perform(MockMvcRequestBuilders.get("/user/1/history")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect((ResultMatcher)jsonPath("$[0].status", Matchers.is("FAILURE")));
    }

    public void successfulTransferIsPersisted() throws Exception {
        // Given no entries in PointTransfer repository
        // And a valid transfer request is made
        Pointtransfer request = new Pointtransfer();
        request.setAmount(10);
        request.setDestination("Company1");

        // When the transfer is requested
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        // Then it is persisted and status is SUCCESS
 		mvc.perform(MockMvcRequestBuilders.get("/user/1/history")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect((ResultMatcher)jsonPath("$[0].status", Matchers.is("SUCCESS")));
    }

    @Test
    public void unsuccessfulTransferIsNotPersisted() throws Exception {
        // Given no entries in PointTransfer repository
        // And a transfer request of an invalid value
        Pointtransfer request = new Pointtransfer();
        request.setAmount(100000);
        request.setDestination("Company1");

        // When a transfer is requested
        // Then the transfer fails and returns HTTP Bad Request
        mvc.perform(post("/user/1/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Then it is persisted and status is FAILURE
 		mvc.perform(MockMvcRequestBuilders.get("/user/1/history")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect((ResultMatcher)jsonPath("$[0].status", Matchers.is("FAILURE")));
    }

}