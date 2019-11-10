package com.beerhouse.dto;

public class BeerDTO {

	private Integer id;
	private String name;
	private String ingredients;
	private String alcoholContent;
	private Double price;
	private String category;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public String getAlcoholContent() {
		return alcoholContent;
	}

	public void setAlcoholContent(String alcoholContent) {
		this.alcoholContent = alcoholContent;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public String toString() {
		return "BeerDTO [id=" + id + ", name=" + name + ", ingredients=" + ingredients + ", alcoholContent="
				+ alcoholContent + ", price=" + price + ", category=" + category + "]";
	}

}
