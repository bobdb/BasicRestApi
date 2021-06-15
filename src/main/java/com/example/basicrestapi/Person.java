package com.example.basicrestapi;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String info;

	public Person() {}

	public Person(String name, String info) {
		this.name=name;
		this.info=info;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String toString() {
		return "Person{" +
				"id=" + id +
				", name='" + name + '\'' +
				", info='" + info + '\'' +
				'}';
	}
}
