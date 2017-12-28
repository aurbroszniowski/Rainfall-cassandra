package io.rainfall.cassandra.operations;

import io.rainfall.AssertionEvaluator;
import io.rainfall.Configuration;
import io.rainfall.ObjectGenerator;
import io.rainfall.Operation;
import io.rainfall.SequenceGenerator;
import io.rainfall.statistics.StatisticsHolder;

import com.datastax.driver.mapping.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static io.rainfall.cassandra.statistics.CassandraResult.SAVE;

/**
 * @author Aurelien Broszniowski
 */

public class InsertOperation<V> implements Operation {
  private final ObjectGenerator<V> valueGenerator;
  private final SequenceGenerator sequenceGenerator;
  private final Mapper<V> mapper;
  private final String name;

  public InsertOperation(final ObjectGenerator<V> valueGenerator,
                         final SequenceGenerator sequenceGenerator, final Mapper<V> mapper) {
    this.valueGenerator = valueGenerator;
    this.sequenceGenerator = sequenceGenerator;
    this.mapper = mapper;
    this.name = this.valueGenerator.generate(1L).getClass().getName();
  }

  @Override
  public void exec(final StatisticsHolder statisticsHolder, final Map<Class<? extends Configuration>, Configuration> configurations,
                   final List<AssertionEvaluator> assertions) {
    long seed = sequenceGenerator.next();
    V value = valueGenerator.generate(seed);
    long start = statisticsHolder.getTimeInNs();
    mapper.save(value);
    long end = statisticsHolder.getTimeInNs();
    statisticsHolder.record(this.name, (end - start), SAVE);
  }

  @Override
  public List<String> getDescription() {
    return Arrays.asList("Save operation", "Entity = " + this.name);
  }
}
