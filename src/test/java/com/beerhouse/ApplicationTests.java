package com.beerhouse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.beerhouse.business.BeerBusiness;
import com.beerhouse.dto.BeerDTO;
import com.beerhouse.exception.DataNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ComponentScan(basePackageClasses = Application.class)
public class ApplicationTests {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private BeerBusiness beerBusiness;

	private ObjectMapper objectMapper;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
		this.objectMapper = new ObjectMapper();
	}

	@Test
	public void testGetBeersSuccess() {
		try {
			MvcResult mvcResult = mockMvc.perform(get("/beers")).andDo(print()).andExpect(status().isOk()).andReturn();
			String data = objectMapper.writeValueAsString(beerBusiness.getBeers());
			String result = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals(data, result);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testPostBeersSuccess() {
		try {
			String body = "{" + "\"name\": \"Cerveja preta\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(post("/beers").content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.CREATED.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testPostBeerPilsenExistsFail() {
		try {
			String body = "{" + "\"name\": \"Pilsen\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(post("/beers").content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testPostBeerNameEmptyFail() {
		try {
			String body = "{" + "\"name\": \"\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(post("/beers").content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testPostBeerPriceNegativeFail() {
		try {
			String body = "{" + "\"name\": \"Cerveja azul\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": -6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(post("/beers").content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetBeerSuccess() {
		try {
			List<BeerDTO> beers = beerBusiness.getBeers();
			BeerDTO beer = null;
			if (!beers.isEmpty()) {
				beer = beers.get(0);
			}
			MvcResult mvcResult = mockMvc.perform(get("/beer/" + beer.getId())).andExpect(status().isOk())
					.andDo(print()).andReturn();
			String data = objectMapper.writeValueAsString(beer);
			String result = mvcResult.getResponse().getContentAsString();
			Assert.assertEquals(data, result);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetBeerFail() {
		try {
			MvcResult mvcResult = mockMvc.perform(get("/beer/-1")).andExpect(status().isNotFound()).andDo(print())
					.andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.NOT_FOUND.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testChangeBeerSuccess() {
		try {
			List<BeerDTO> beers = beerBusiness.getBeers();
			BeerDTO beer = null;
			if (!beers.isEmpty()) {
				beer = beers.get(0);
			}
			String body = "{\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(patch("/beer/" + beer.getId()).content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.OK.value());
			BeerDTO result = beerBusiness.getBeer(beer.getId());
			Assert.assertEquals(result.getIngredients(), "Cevada, lúpudo e malte");
			Assert.assertEquals(result.getAlcoholContent(), "12%");
			Assert.assertEquals(result.getPrice(), new Double(6.8));
			Assert.assertEquals(result.getCategory(), "Forte");
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testChangeBeerFail() {
		try {
			String body = "{\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(patch("/beer/-1").content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.NOT_FOUND.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testUpdateBeerSuccess() {
		try {
			List<BeerDTO> beers = beerBusiness.getBeers();
			BeerDTO beer = null;
			if (!beers.isEmpty()) {
				beer = beers.get(0);
			}			
			String body = "{" + "\"id\":" + beer.getId() + ",\"name\": \"Cerveja preta\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(put("/beer/" + beer.getId()).content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.OK.value());
			BeerDTO result = beerBusiness.getBeer(beer.getId());
			Assert.assertEquals(result.getName(), "Cerveja preta");
			Assert.assertEquals(result.getIngredients(), "Cevada, lúpudo e malte");
			Assert.assertEquals(result.getAlcoholContent(), "12%");
			Assert.assertEquals(result.getPrice(), new Double(6.8));
			Assert.assertEquals(result.getCategory(), "Forte");
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testUpdateBeerNameIsEmptyFail() {
		try {
			List<BeerDTO> beers = beerBusiness.getBeers();
			BeerDTO beer = null;
			if (!beers.isEmpty()) {
				beer = beers.get(0);
			}			
			String body = "{" + "\"id\":" + beer.getId() + ",\"name\": \"\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(put("/beer/" + beer.getId()).content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testUpdateBeerPriceNegativeFail() {
		try {
			List<BeerDTO> beers = beerBusiness.getBeers();
			BeerDTO beer = null;
			if (!beers.isEmpty()) {
				beer = beers.get(0);
			}			
			String body = "{" + "\"id\":" + beer.getId() + ",\"name\": \"Cerveja preta\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": -6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(put("/beer/" + beer.getId()).content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.BAD_REQUEST.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testUpdateBeerFail() {
		try {
			String body = "{" + "\"id\": -1," + "\"name\": \"Cerveja preta\"," + "\"ingredients\": \"Cevada, lúpudo e malte\","
					+ "\"alcoholContent\": \"12%\"," + "\"price\": 6.8," + "\"category\": \"Forte\"" + "}";
			MvcResult mvcResult = mockMvc.perform(put("/beer/-1").content(body).contentType(MediaType.APPLICATION_JSON))
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.NOT_FOUND.value());
		} catch (Exception e) {
			Assert.fail();
		}
	}
	

	@Test
	public void testDeleteBeer() {
		try {
			List<BeerDTO> beers = beerBusiness.getBeers();
			BeerDTO beer = null;
			if (!beers.isEmpty()) {
				beer = beers.get(0);
			}
			MvcResult mvcResult = mockMvc.perform(delete("/beer/" + beer.getId())).andExpect(status().isNoContent())
					.andDo(print()).andReturn();
			int status = mvcResult.getResponse().getStatus();
			Assert.assertEquals(status, HttpStatus.NO_CONTENT.value());
			beerBusiness.getBeer(beer.getId());
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), DataNotFoundException.class);
		}
	}
}