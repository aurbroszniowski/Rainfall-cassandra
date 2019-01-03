/*
 * Copyright (c) 2014-2019 Aurélien Broszniowski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.rainfall.entity;


import com.datastax.driver.mapping.annotations.Table;

import java.util.concurrent.ThreadLocalRandom;

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
