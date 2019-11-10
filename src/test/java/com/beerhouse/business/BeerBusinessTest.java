package com.beerhouse.business;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.beerhouse.business.impl.BeerBusinessImpl;
import com.beerhouse.dto.BeerDTO;
import com.beerhouse.exception.DataAlreadyExistsException;
import com.beerhouse.exception.DataNotFoundException;
import com.beerhouse.exception.InvalidFieldValueException;
import com.beerhouse.service.BeerService;

@RunWith(SpringRunner.class)
public class BeerBusinessTest {

	@TestConfiguration
	public static class BeerBusinessTestConfiguration {

		@Bean
		public BeerBusiness beerBusiness() {
			return new BeerBusinessImpl();
		}
	}

	@Autowired
	private BeerBusiness beerBusiness;

	@MockBean
	private BeerService beerService;

	@Test
	public void testGetBeersSuccess() {
		List<BeerDTO> data = new ArrayList<BeerDTO>();
		BeerDTO item = new BeerDTO();
		item.setId(1);
		item.setName("teste");
		data.add(item);
		Mockito.when(beerService.getBeers()).thenReturn(data);
		List<BeerDTO> result = beerBusiness.getBeers();
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 1);
		Assert.assertEquals(result.get(0).getName(), "teste");
	}

	@Test
	public void testGetBeersEmptySuccess() {
		List<BeerDTO> data = new ArrayList<BeerDTO>();
		Mockito.when(beerService.getBeers()).thenReturn(data);
		List<BeerDTO> result = beerBusiness.getBeers();
		Assert.assertNotNull(result);
		Assert.assertEquals(result.size(), 0);
	}

	@Test
	public void testGetBeerSuccess() {
		try {
			BeerDTO data = new BeerDTO();
			data.setId(1);
			data.setName("teste");
			Mockito.when(beerService.getBeer(1)).thenReturn(data);
			BeerDTO result = beerBusiness.getBeer(1);
			Assert.assertNotNull(result);
			Assert.assertEquals(result.getId(), data.getId());
			Assert.assertEquals(result.getName(), data.getName());
		} catch (DataNotFoundException e) {
			Assert.fail();
		}
	}

	@Test
	public void testGetBeerFail() {
		try {
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			beerBusiness.getBeer(1);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), DataNotFoundException.class);
		}
	}

	@Test
	public void testDeleteBeerSuccess() {
		try {
			beerBusiness.deleteBeer(1);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testCreateBeerSuccess() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setName("teste");
			newBeer.setPrice(1.0);
			beerBusiness.createBeer(newBeer);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testCreateBeerWithInvalidIdSuccess() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setId(999);
			newBeer.setName("teste");
			newBeer.setPrice(1.0);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.getBeerByName(Mockito.anyString())).thenReturn(null);
			beerBusiness.createBeer(newBeer);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testCreateBeerWithInvalidNameSuccess() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setName("teste");
			newBeer.setPrice(1.0);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.getBeerByName("found")).thenReturn(found);
			beerBusiness.createBeer(newBeer);
			Assert.assertTrue(true);
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testCreateBeerIdExistsFail() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setId(1);
			newBeer.setName("teste");
			newBeer.setPrice(1.0);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.getBeerByName(Mockito.anyString())).thenReturn(null);
			beerBusiness.createBeer(newBeer);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), DataAlreadyExistsException.class);
		}
	}

	@Test
	public void testCreateBeerWithNameExistsFail() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setName("found");
			newBeer.setPrice(1.0);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.getBeerByName("found")).thenReturn(found);
			beerBusiness.createBeer(newBeer);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), DataAlreadyExistsException.class);
		}
	}

	@Test
	public void testCreateBeerWithNameEmptyFail() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setName("");
			newBeer.setPrice(1.0);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.getBeerByName(Mockito.anyString())).thenReturn(null);
			beerBusiness.createBeer(newBeer);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidFieldValueException.class);
		}
	}
	
	@Test
	public void testCreateBeerWithPriceNullFail() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setName("teste");
			newBeer.setPrice(null);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.getBeerByName(Mockito.anyString())).thenReturn(null);
			beerBusiness.createBeer(newBeer);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidFieldValueException.class);
		}
	}
	
	@Test
	public void testCreateBeerWithNegativePriceFail() {
		try {
			BeerDTO newBeer = new BeerDTO();
			newBeer.setName("teste");
			newBeer.setPrice(-1.0);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.getBeerByName(Mockito.anyString())).thenReturn(null);
			beerBusiness.createBeer(newBeer);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidFieldValueException.class);
		}
	}

	@Test
	public void testUpdateBeerSuccess() {
		try {
			BeerDTO update = new BeerDTO();
			update.setName("teste");
			update.setPrice(1.0);
			update.setId(1);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("teste");
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.updateBeer(1, update);
			Assert.assertEquals(update.getName(), "teste");
			Assert.assertEquals(update.getPrice(), new Double(1.0));
			Assert.assertEquals(update.getId(), new Integer(1));
		} catch (Exception e) {
			Assert.fail();
		}
	}

	@Test
	public void testUpdateBeerWithIdWrongSuccess() {
		try {
			BeerDTO update = new BeerDTO();
			update.setName("teste");
			update.setPrice(1.0);
			update.setId(999);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("teste");
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.updateBeer(1, update);
			Assert.assertEquals(update.getName(), "teste");
			Assert.assertEquals(update.getPrice(), new Double(1.0));
			Assert.assertEquals(update.getId(), new Integer(1));
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testUpdateBeerWithNameEmptyFail() {
		try {
			BeerDTO update = new BeerDTO();
			update.setName("");
			update.setPrice(1.0);
			update.setId(999);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("teste");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.updateBeer(1, update);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidFieldValueException.class);
		}
	}
	
	@Test
	public void testUpdateBeerWithPriceNullFail() {
		try {
			BeerDTO update = new BeerDTO();
			update.setName("teste");
			update.setPrice(null);
			update.setId(999);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("teste");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.updateBeer(1, update);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidFieldValueException.class);
		}
	}
	
	@Test
	public void testUpdateBeerWithNegaticePriceFail() {
		try {
			BeerDTO update = new BeerDTO();
			update.setName("teste");
			update.setPrice(-1.0);
			update.setId(999);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("teste");
			found.setPrice(1.0);
			Mockito.when(beerService.getBeer(1)).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.updateBeer(1, update);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), InvalidFieldValueException.class);
		}
	}

	@Test
	public void testUpdateBeerNotExistsFail() {
		try {
			BeerDTO update = new BeerDTO();
			update.setName("teste");
			update.setPrice(1.0);
			update.setId(999);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.updateBeer(999, update);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), DataNotFoundException.class);
		}
	}

	@Test
	public void testChangeBeerPriceSuccess() {
		try {
			BeerDTO change = new BeerDTO();
			change.setPrice(1.0);
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(0.5);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.changeBeer(1, change);
			Assert.assertEquals(change.getName(), "found");
			Assert.assertEquals(change.getPrice(), new Double(1.0));
			Assert.assertEquals(change.getId(), new Integer(1));
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testChangeBeerAllSuccess() {
		try {
			BeerDTO change = new BeerDTO();
			change.setPrice(1.0);
			change.setAlcoholContent("12%");
			change.setCategory("Fraca");
			change.setIngredients("Malte");
			change.setName("teste");
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(0.5);
			found.setAlcoholContent("10%");
			found.setCategory("Forte");
			found.setIngredients("Pinho");
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.changeBeer(1, change);
			Assert.assertEquals(change.getName(), "teste");
			Assert.assertEquals(change.getPrice(), new Double(1.0));
			Assert.assertEquals(change.getId(), new Integer(1));
			Assert.assertEquals(change.getAlcoholContent(), "12%");
			Assert.assertEquals(change.getCategory(), "Fraca");
			Assert.assertEquals(change.getIngredients(), "Malte");
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testChangeBeerNotChangeSuccess() {
		try {
			BeerDTO change = new BeerDTO();
			BeerDTO found = new BeerDTO();
			found.setId(1);
			found.setName("found");
			found.setPrice(1.0);
			found.setAlcoholContent("10%");
			found.setCategory("Forte");
			found.setIngredients("Pinho");
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(found);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(1);
			beerBusiness.changeBeer(1, change);
			Assert.assertEquals(change.getName(), "found");
			Assert.assertEquals(change.getPrice(), new Double(1.0));
			Assert.assertEquals(change.getId(), new Integer(1));
			Assert.assertEquals(change.getAlcoholContent(), "10%");
			Assert.assertEquals(change.getCategory(), "Forte");
			Assert.assertEquals(change.getIngredients(), "Pinho");
		} catch (Exception e) {
			Assert.fail();
		}
	}
	
	@Test
	public void testChangeBeerNotFoundFail() {
		try {
			BeerDTO change = new BeerDTO();
			change.setId(999);
			change.setName("found");
			change.setPrice(1.0);
			Mockito.when(beerService.getBeer(Mockito.anyInt())).thenReturn(null);
			Mockito.when(beerService.saveBeer(Mockito.any(BeerDTO.class))).thenReturn(-1);
			beerBusiness.changeBeer(999, change);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getClass(), DataNotFoundException.class);
		}
	}

}
