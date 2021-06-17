/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	  https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.basicrestapi;

import static org.assertj.core.internal.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class MainControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	public void init() {
		//reset User1 with 999 starting balance and clear history
		try {
			URL url = new URL("http://localhost:8080/user/1/reset");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			int status = con.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void ProgramsEndpointShouldReturnTestCompanies() throws Exception {
//		mockMvc.perform(get("/user/1/programs")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher)jsonPath("$[0].name", is("Company1")));
//
//		mockMvc.perform(get("/user/1/programs")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher) jsonPath("$[1].name", is("Company2")))
//				.andExpect((ResultMatcher) jsonPath("$[1].exchangeRate", is(3.0)));
//
//		mockMvc.perform(get("/user/1/programs")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher) jsonPath("$[2].name", is("Company3")))
//				.andExpect((ResultMatcher) jsonPath("$[2].exchangeRate", is(0.5)));
	}

	@Test
	public void fundWithValidAmountIsOk() throws Exception {

//			FundingRequest request = new FundingRequest();
//			request.setAmount(123);
//			mockMvc.perform(post("/user/1/fund")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isOk())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("SUCCESS")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999));
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(123));
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(1122));
	}

	@Test
	public void fundWithNegativeOrZeroFails() throws Exception {

//			FundingRequest request = new FundingRequest();
//			request.setAmount(-1);
//			mockMvc.perform(post("/user/1/fund")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].message", is("Bad funding"));
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(-1));
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999));
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999));

//			FundingRequest request = new FundingRequest();
//			request.setAmount(-1);
//			mockMvc.perform(post("/user/1/fund")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999))
//					.andExpect((ResultMatcher) jsonPath("$[0].message", is("Bad funding")
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(-1))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999));

//				request.setAmount(0);
//				mockMvc.perform(post("/user/1/fund")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999))
//					.andExpect((ResultMatcher) jsonPath("$[0].message", is("Bad funding")
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(0))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999));
	}


	@Test
	public void transferWithValidRequestSucceeds() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(100);
//			request.setDestination("Company1");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isOk())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("SUCCESS")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(899)))
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(100)));

	}

	@Test
	public void transferWithNegativeOrZeroFails() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(-1);
//			request.setDestination("Company1");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(-1)));

//			request.setAmount(0);
//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(0)));	}
	}

	@Test
	public void transferWhenExchangeRateCausesLessThanOnePointFails() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(1);
//			request.setDestination("Company3");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(1)));	}
	}

	@Test
	public void transferWhenNotSubscribedToProgramFails() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(1);
//			request.setDestination("NotACompany");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(1)));
	}
	@Test
	public void transferWhenBalanceIsTooLowFails() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(100000);
//			request.setDestination("Company1");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//					.andExpect(content()
//							.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//					.andExpect((ResultMatcher) jsonPath("$[0].status", is("FAILURE")))
//					.andExpect((ResultMatcher) jsonPath("$[0].startingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].endingBalance", is(999)))
//					.andExpect((ResultMatcher) jsonPath("$[0].amount", is(100000)));
				}

	@Test
	public void HistoryReturnsSuccessfullyWhetherEmptyOrPopulated() throws Exception {
//			mockMvc.perform(get("/user/1/history")
//					.contentType(MediaType.APPLICATION_JSON)
//					.andExpect(status().isOk())
	}

	@Test
	public void successfulFundingIsPersisted() throws Exception {
//			FundingRequest request = new FundingRequest();
//			request.setAmount(123);
//			mockMvc.perform(post("/user/1/fund")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isOk())
//
// 		mockMvc.perform(get("/user/1/history")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher)jsonPath("$[0].status", is("SUCCESS")));

	}

	@Test
	public void unsuccessfulFundingIsNotPersisted() throws Exception {
//			FundingRequest request = new FundingRequest();
//			request.setAmount(-123);
//			mockMvc.perform(post("/user/1/fund")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//
// 		mockMvc.perform(get("/user/1/history")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher)jsonPath("$[0].status", is("FAILURE")));
	}

	public void successfulTransferIsPersisted() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(10);
//			request.setDestination("Company1");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isOk())
//
// 		mockMvc.perform(get("/user/1/history")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher)jsonPath("$[0].status", is("SUCCESS")));
}

	@Test
	public void unsuccessfulTransferIsNotPersisted() throws Exception {
//			Pointtransfer request = new Pointtransfer();
//			request.setAmount(100000);
//			request.setDestination("Company1");

//			mockMvc.perform(post("/user/1/transfer")
//					.contentType(MediaType.APPLICATION_JSON)
//					.content(objectMapper.writeValueAsString(request)))
//					.andExpect(status().isBadResponse())
//
// 		mockMvc.perform(get("/user/1/history")
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isOk())
//				.andExpect(content()
//						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//				.andExpect((ResultMatcher)jsonPath("$[0].status", is("FAILURE")));
	}


}
