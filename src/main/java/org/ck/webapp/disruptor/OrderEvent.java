package org.ck.webapp.disruptor;

/**
 * Created by hll on 2016/8/1.
 */
public class OrderEvent {
	private Long id;
	private String username;
	private Double price;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "OrderEvent{" + "id=" + id + ", username='" + username + '\'' + ", price=" + price + '}';
	}
}
