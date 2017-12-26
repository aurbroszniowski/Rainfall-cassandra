package io.rainfall.entity;

import jsr166e.ThreadLocalRandom;

import com.datastax.driver.mapping.annotations.Table;

/**
 * @author Aurelien Broszniowski
 */

@Table(name = "person")
public class Person {

  private Long id;
  private String name;
  private String firstname;

  public Person(final ThreadLocalRandom random, final long seed) {
    this.id = seed;
    this.name = "Name" + random.nextLong(seed);
    this.firstname = "FirstName" + random.nextLong(seed);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getFirstname() {
    return firstname;
  }
}
