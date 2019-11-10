package com.beerhouse.business.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.beerhouse.business.BeerBusiness;
import com.beerhouse.dto.BeerDTO;
import com.beerhouse.exception.DataAlreadyExistsException;
import com.beerhouse.exception.DataNotFoundException;
import com.beerhouse.exception.InvalidFieldValueException;
import com.beerhouse.service.BeerService;

@Component
public class BeerBusinessImpl implements BeerBusiness {

	private static final Log log = LogFactory.getLog(BeerBusinessImpl.class);
	@Autowired
	private BeerService beerService;

	@Override
	public List<BeerDTO> getBeers() {
		log.debug("[START] getBeers: List<BeerDTO>");
		List<BeerDTO> beers = beerService.getBeers();
		log.debug("[END] getBeers: List<BeerDTO>");
		return beers;
	}

	@Override
	public BeerDTO getBeer(Integer id) throws DataNotFoundException {
		log.debug("[START] getBeer(" + id + "): BeerDTO");
		BeerDTO beer = beerService.getBeer(id);
		if (beer != null) {
			log.debug("[END] getBeer(" + id + "): BeerDTO");
			return beer;
		} else {
			log.warn("[WARNING] Data not found");
			throw new DataNotFoundException();
		}
	}

	@Override
	public void createBeer(BeerDTO beer) throws DataAlreadyExistsException, InvalidFieldValueException {
		log.debug("[START] createBeer(" + beer + "): Void");
		if (beer.getId() != null) {
			if (beerService.getBeer(beer.getId()) != null) {
				log.warn("[WARNING] Data already exists");
				throw new DataAlreadyExistsException();
			} else {
				beer.setId(null);
			}
		}
		if (!StringUtils.isEmpty(beer.getName())) {
			if (beerService.getBeerByName(beer.getName()) != null) {
				log.warn("[WARNING] Data already exists");
				throw new DataAlreadyExistsException();
			}
		} else {
			throw new InvalidFieldValueException();
		}
		if (beer.getPrice() == null || beer.getPrice() < 0.0) {
			throw new InvalidFieldValueException();
		}
		Integer newId = beerService.saveBeer(beer);
		beer.setId(newId);
		log.debug("[END] createBeer(" + beer + "): Void");
	}

	@Override
	public void updateBeer(Integer id, BeerDTO beer) throws DataNotFoundException, InvalidFieldValueException {
		log.debug("[START] updateBeer(" + id + "," + beer + "): Void");
		if (beerService.getBeer(id) != null) {
			beer.setId(id);
			if (StringUtils.isEmpty(beer.getName())) {
				throw new InvalidFieldValueException();
			}
			if (beer.getPrice() == null || beer.getPrice() < 0.0) {
				throw new InvalidFieldValueException();
			}
			Integer newId = beerService.saveBeer(beer);
			beer.setId(newId);
			log.debug("[END] updateBeer(" + id + "," + beer + "): Void");
		} else {
			log.warn("[WARNING] Data not found");
			throw new DataNotFoundException();
		}
	}

	@Override
	public void changeBeer(Integer id, BeerDTO beer) throws DataNotFoundException {
		log.debug("[START] changeBeer(" + id + "," + beer + "): Void");
		BeerDTO current = beerService.getBeer(id);
		if (current != null) {
			beer.setId(id);
			if (StringUtils.isEmpty(beer.getAlcoholContent())) {
				beer.setAlcoholContent(current.getAlcoholContent());
			}
			if (StringUtils.isEmpty(beer.getCategory())) {
				beer.setCategory(current.getCategory());
			}
			if (StringUtils.isEmpty(beer.getIngredients())) {
				beer.setIngredients(current.getIngredients());
			}
			if (StringUtils.isEmpty(beer.getName())) {
				beer.setName(current.getName());
			}
			if (beer.getPrice() == null) {
				beer.setPrice(current.getPrice());
			}
			Integer newId = beerService.saveBeer(beer);
			beer.setId(newId);
			log.debug("[END] changeBeer(" + id + "," + beer + "): Void");
		} else {
			log.warn("[WARNING] Data not found");
			throw new DataNotFoundException();
		}
	}

	@Override
	public void deleteBeer(Integer id) {
		log.debug("[START] deleteBeer(" + id + "): Void");
		beerService.deleteBeer(id);
		log.debug("[END] deleteBeer(" + id + "): Void");
	}

}
