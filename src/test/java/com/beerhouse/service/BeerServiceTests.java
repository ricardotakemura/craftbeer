package com.beerhouse.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.dto.BeerDTO;
import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;
import com.beerhouse.service.impl.BeerServiceDatabaseImpl;

@RunWith(SpringRunner.class)
public class BeerServiceTests {

	@TestConfiguration
	static class BeerServiceTestsConfiguration {
		@Bean
		public BeerService beerService() {
			return new BeerServiceDatabaseImpl();
		}
	}

	@Autowired
	private BeerService beerService;

	@MockBean
	private BeerRepository beerRepository;

	@Test
	public void testGetBeersWithOneDataSuccess() {
		List<Beer> data = new ArrayList<Beer>();
		Beer item = new Beer();
		item.setName("teste");
		item.setId(1);
		data.add(item);
		Mockito.when(beerRepository.findAll()).thenReturn(data);
		List<BeerDTO> beers = beerService.getBeers();
		Assert.assertNotNull(beers);
		Assert.assertEquals(beers.size(), 1);
		Assert.assertEquals(beers.get(0).getName(), "teste");
	}

	@Test
	public void testGetBeersWithoutDataSuccess() {
		List<Beer> data = new ArrayList<Beer>();
		Mockito.when(beerRepository.findAll()).thenReturn(data);
		List<BeerDTO> beers = beerService.getBeers();
		Assert.assertNotNull(beers);
		Assert.assertEquals(beers.size(), 0);
	}

	@Test
	public void testGetBeerSuccess() {
		Beer beer = new Beer();
		beer.setName("teste");
		beer.setId(1);
		Mockito.when(beerRepository.getOne(1)).thenReturn(beer);
		BeerDTO result = beerService.getBeer(1);
		Assert.assertNotNull(result);
		Assert.assertEquals(beer.getId(), new Integer(1));
	}

	@Test
	public void testGetBeerFail() {
		Mockito.when(beerRepository.getOne(Mockito.anyInt())).thenThrow(new EntityNotFoundException());
		BeerDTO result = beerService.getBeer(2);
		Assert.assertNull(result);
	}

	@Test
	public void testGetBeerByNameSuccess() {
		Beer beer = new Beer();
		beer.setName("teste");
		beer.setId(1);
		Mockito.when(beerRepository.findByName("teste")).thenReturn(beer);
		BeerDTO result = beerService.getBeerByName("teste");
		Assert.assertNotNull(result);
		Assert.assertEquals(beer.getName(), "teste");
	}

	@Test
	public void testGetBeerByNameFail() {
		Mockito.when(beerRepository.findByName(Mockito.anyString())).thenThrow(new EntityNotFoundException());
		BeerDTO result = beerService.getBeerByName("teste");
		Assert.assertNull(result);
	}

	@Test
	public void testSaveNewBeerSuccess() {
		Beer newBeer = new Beer();
		newBeer.setId(1);
		newBeer.setName("teste");
		BeerDTO insert = new BeerDTO();
		insert.setName("teste");
		Mockito.when(beerRepository.saveAndFlush(Mockito.any(Beer.class))).thenReturn(newBeer);
		Integer newId = beerService.saveBeer(insert);
		Assert.assertEquals(newId, new Integer(1));
	}

	@Test
	public void testDeleteBeerSuccess() {
		try {
			beerService.deleteBeer(1);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

}
