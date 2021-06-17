package com.example.basicrestapi;

import javax.persistence.*;
import java.util.List;

/**
 * Persisted Entity
 * NOTE:  The collection of Loyalty Programs that a User is subscribed to SHOULD
 * 		  be one-to-many FK Column in this entity.  For the time being, it is mocked
 * 		  within the UserService.  The code to add back a column has been commented out.
 */
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private long points;

	//private List<Program> programs;

	public User() {}

	public User(String name, long points) {
		this.name=name;
		this.points=points;
	//	this.programs=programs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPoints() {
		return points;
	}

	public void setPoints(long points) {
		this.points = points;
	}

//	public List<Program> getPrograms() {
//		return programs;
//	}
//
//	public void setPrograms(List<Program> programs) {
//		this.programs = programs;
//	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", name='" + name + '\'' +
				", points=" + points  +
				//", programs=[" + programs + ']' +
				'}';
	}
}
