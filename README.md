# HLLAccumulator
HyperLogLog-based accumulator for Apache Spark

A custom AccumulatorV2 implementation that uses HyperLogLogPlus to count unique elements seen.
It provides a cardinality estimation of the observed elements.

## Local deployment

This sample application is meant to be deployed local, using the sbt publishing function

```bash
sbt publishLocal

```