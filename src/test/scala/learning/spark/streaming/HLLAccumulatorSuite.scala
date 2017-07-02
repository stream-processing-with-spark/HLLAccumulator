package learning.spark.streaming

import org.scalatest.{Matchers, WordSpec}

import scala.util.Random

class HLLAccumulatorSuite extends WordSpec with Matchers {

  "HLLAccumulator" should {

    val Error = 0.05 // 5%
    val TestElements = 10000

    def genSample() = List.fill[Int](TestElements)(scala.util.Random.nextInt(TestElements))

    def createAccumulatorWithElements[T](elems : Seq[T]):HLLAccumulator[T] = {
      val acc = new HLLAccumulator[T]()
      elems.foreach(e => acc.add(e))
      acc
    }

    def beWithinTolerance(value: Long) = {
      val errorBound = Math.ceil(value * Error/2).toLong
      be >= (value - errorBound) and be <= (value + errorBound)
    }


    "be initialized as the zero element" in {
      val hllAccumulator = new HLLAccumulator[String]()
      assert(hllAccumulator.isZero)
    }

    "estimate unique element frequency" in {
      val sample = genSample()
      val uniqueElements = sample.toSet.size
      val hllAccumulator = createAccumulatorWithElements(sample)

      println("unique elements: "+ uniqueElements)
      println("estimated unique elements: "+ hllAccumulator.value )

      hllAccumulator.value should beWithinTolerance(uniqueElements)
    }

    "merge two estimates" in {
      val sample = genSample()
      val uniqueElements = sample.toSet.size

      // random partition the sample
      val (part1, part2) = genSample().partition(_ => Random.nextBoolean())
      val hllAcc1 = createAccumulatorWithElements(part1)
      val hllAcc2 = createAccumulatorWithElements(part2)

      hllAcc1.value shouldNot beWithinTolerance(uniqueElements)
      hllAcc2.value shouldNot beWithinTolerance(uniqueElements)

      hllAcc1.merge(hllAcc2) // in-place merge
      hllAcc1.value should beWithinTolerance(uniqueElements)

    }

    "produce a copy" in {
      val sample = genSample()
      val uniqueElements = sample.toSet.size
      val hllAccCopy = createAccumulatorWithElements(sample).copy()
      hllAccCopy.value should beWithinTolerance(uniqueElements)
    }

    "reset the accumulator" in {
      val sample = genSample()
      val hllAcc = createAccumulatorWithElements(sample)
      require(!hllAcc.isZero)
      hllAcc.reset()
      assert(hllAcc.isZero)
    }

  }


}
