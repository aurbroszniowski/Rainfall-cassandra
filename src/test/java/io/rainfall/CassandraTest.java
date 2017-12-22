package io.rainfall;

import org.junit.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * @author Aurelien Broszniowski
 */

public class CassandraTest {


  @Test
  public void simpleConnect() {
    Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
    Session session = cluster.connect();
    session.execute("DROP KEYSPACE cassandratest;");
    session.execute("CREATE KEYSPACE cassandratest WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };");
    session.execute("USE cassandratest;");
    session.execute("CREATE TABLE person (id bigint PRIMARY KEY, name text);");
    session.execute("INSERT INTO person (id, name) VALUES (1, 'jack');");


    session.execute("use cassandratest");

    ResultSet resultSet = session.execute("select * from person");
    session.close();
    cluster.close();
  }

}
