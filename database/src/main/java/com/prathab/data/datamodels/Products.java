package com.prathab.data.datamodels;

import com.prathab.data.base.dbmodel.DbObject;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Products implements DbObject {
	private String id;
	private String name;
	private String price;
	private int rating;
	private ArrayList<String> images;
	private ArrayList<String> tags;
	private String description;

	public Products() {
	}

	public Products(String id, String name, String price, int rating, ArrayList<String> images, ArrayList<String> tags,
			String description) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
		this.images = images;
		this.tags = tags;
		this.description = description;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Products{");
		sb.append("id='").append(id).append('\'');
		sb.append(", name='").append(name).append('\'');
		sb.append(", price='").append(price).append('\'');
		sb.append(", rating=").append(rating);
		sb.append(", images=").append(images);
		sb.append(", tags=").append(tags);
		sb.append(", description='").append(description).append('\'');
		sb.append('}');
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Products products = (Products) o;

		return id.equals(products.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
