package com.beerhouse.repository;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.entity.Beer;
import com.beerhouse.repository.BeerRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BeerRepositoryTests {
	
	@Autowired
	private BeerRepository beerRepository;
	
	@Test
	public void testFindAllSuccess() {
	    List<Beer> beers = beerRepository.findAll();
	    Assert.assertNotNull(beers);
	    Assert.assertEquals(beers.size(), 5);
	}
	
	@Test
	public void testFindByNameSuccess() {
	    Beer beer = beerRepository.findByName("Frutada");
	    Assert.assertNotNull(beer);
	    Assert.assertEquals(beer.getName(), "Frutada");
	}

	@Test
	public void testGetOneSuccess() {
		Beer pilsen = beerRepository.findByName("Pilsen");
	    Beer beer = beerRepository.getOne(pilsen.getId());
	    Assert.assertNotNull(beer);
	    Assert.assertEquals(beer.hashCode(), pilsen.hashCode());
	    Assert.assertEquals(beer, pilsen);
	}
	
	@Test
	public void testSaveAndFlushWithNewBeerSuccess() {
		Beer beer = new Beer();
		beer.setId(null);
		beer.setName("Teste");
		beer.setAlcoholContent("1%");
		beer.setCategory("Fraca");
		beer.setIngredients("teste");
		beer.setPrice(1.0);
		Beer saved = beerRepository.saveAndFlush(beer);
		Assert.assertNotNull(saved.getId());
		Assert.assertEquals(beer.hashCode(), saved.hashCode());
		Assert.assertEquals(beer, saved);
	}

	@Test
	public void testSaveAndFlushWithBeerPilsenSuccess() {
		Beer beer = beerRepository.findByName("Pilsen");
		beer.setAlcoholContent("1%");
		beer.setCategory("Fraca");
		beer.setIngredients("teste");
		beer.setPrice(1.0);
		Beer saved = beerRepository.saveAndFlush(beer);
		Assert.assertEquals(saved.getName(), "Pilsen");
		Assert.assertEquals(beer.hashCode(), saved.hashCode());
		Assert.assertEquals(beer, saved);
	}

	@Test
	public void testDeleteSuccess() {
		Beer beer = new Beer();
		beer.setId(null);
		beer.setName("Teste");
		beer.setAlcoholContent("1%");
		beer.setCategory("Fraca");
		beer.setIngredients("teste");
		beer.setPrice(1.0);
		Beer saved = beerRepository.saveAndFlush(beer);
		beerRepository.delete(saved);
		Beer deleted = beerRepository.findByName("Teste");
		Assert.assertNull(deleted);
	}
}
