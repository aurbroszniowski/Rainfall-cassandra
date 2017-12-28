package io.rainfall.cassandra;

import io.rainfall.ObjectGenerator;
import io.rainfall.Operation;
import io.rainfall.SequenceGenerator;
import io.rainfall.cassandra.operations.InsertOperation;

import com.datastax.driver.mapping.Mapper;

/**
 * @author Aurelien Broszniowski
 */

public class CassandraOperations {

  public static <V> Operation insert(final ObjectGenerator<V> valueGenerator,
                                        final SequenceGenerator sequenceGenerator, final Mapper<V> mapper) {
    return new InsertOperation<V>(valueGenerator, sequenceGenerator, mapper);
  }

}
