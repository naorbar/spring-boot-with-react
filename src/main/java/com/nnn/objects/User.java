package com.nnn.objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

	@Id
	private String id;
	private String name;
	private String title;

	public User() {

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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public static class Builder {
		private String id;
		private String name;
		private String title;

		public Builder withId(String id) {
			this.id = id;
			return this;
		}

		public Builder withName(String name) {
			this.name = name;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public User build() {
			return new User(this);
		}
	}

	private User(Builder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.title = builder.title;
	}
}
