/*
 * Copyright 2014 Aurélien Broszniowski
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

package io.rainfall;

import io.rainfall.cassandra.CassandraOperations;
import io.rainfall.configuration.ConcurrencyConfig;
import io.rainfall.configuration.ReportingConfig;
import io.rainfall.cassandra.statistics.CassandraResult;
import io.rainfall.entity.Person;
import io.rainfall.entity.PersonGenerator;
import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

import static io.rainfall.configuration.ReportingConfig.html;
import static io.rainfall.execution.Executions.during;
import static io.rainfall.generator.SequencesGenerator.sequentially;
import static io.rainfall.unit.TimeDivision.minutes;
import static java.util.concurrent.TimeUnit.MINUTES;

/**
 * @author Aurelien Broszniowski
 */

public class CassandraTest {

  @Test
  public void simpleLoad() throws SyntaxException {
    Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
    Session session = cluster.connect();
    session.execute("DROP KEYSPACE cassandratest;");
    session.execute("CREATE KEYSPACE cassandratest WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };");
    session.execute("USE cassandratest;");
    session.execute("CREATE TABLE person (id bigint PRIMARY KEY, firstname text, name text);");

    MappingManager manager = new MappingManager(session);
    Mapper<Person> mapper = manager.mapper(Person.class);

    ObjectGenerator<Person > valueGenerator = new PersonGenerator();

    ConcurrencyConfig concurrency = ConcurrencyConfig.concurrencyConfig().threads(4).timeout(50, MINUTES);

    ScenarioRun run = Runner.setUp(
        Scenario.scenario("Warm up phase").exec(
            CassandraOperations.insert(valueGenerator, sequentially(), mapper)
        ))
        .warmup(during(5, minutes))
        .executed(during(5, minutes))
        .config(concurrency, ReportingConfig.report(CassandraResult.class).log(html()));

    run.start();
    session.close();
    cluster.close();
  }


}
