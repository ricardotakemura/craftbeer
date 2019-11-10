package com.beerhouse.business;

import java.util.List;

import com.beerhouse.dto.BeerDTO;
import com.beerhouse.exception.DataAlreadyExistsException;
import com.beerhouse.exception.DataNotFoundException;
import com.beerhouse.exception.InvalidFieldValueException;

public interface BeerBusiness {

	List<BeerDTO> getBeers();

	BeerDTO getBeer(Integer id) throws DataNotFoundException;

	void createBeer(BeerDTO beer) throws DataAlreadyExistsException, InvalidFieldValueException;

	void updateBeer(Integer id, BeerDTO beer) throws DataNotFoundException, InvalidFieldValueException;

	void changeBeer(Integer id, BeerDTO beer) throws DataNotFoundException;

	void deleteBeer(Integer id);
}
