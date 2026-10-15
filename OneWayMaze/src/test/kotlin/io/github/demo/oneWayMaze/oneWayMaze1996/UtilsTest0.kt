//package test.kotlin.io.github.demo.oneWayMaze.oneWayMaze1996
//
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.fail
//import java.util.Random
//import kotlin.math.roundToInt
//
//class UtilsTest {
//
//    @Test fun testBinarySearch(){
//        val rand = JavaRandom(1)
//        // High divisor means
//        val divisor = 3
//
//        val rangeSizes: Sequence<Int> = (0..99).asSequence() + exponentiallyAscendingInts(100, 1.1)))
//        for ( rangeSize in raS) {
//
//                val actual =
//            }
//
//        }
//    }
//
//    private fun testBinarySearch(rand: JavaRandom, rangeSize:Int, lowestSatisfying:Int?){
//        val low = rand.nextIntIn(-200..200)
//        val high = low+rangeSize-1
//        val range = low..high
//
//        val lowestSatisfyingRange =
//
//        for (lowestSatisfying in if (lowestSatisfying))
//        val lowestSatisfying = if ()
//        val bools = List(rangeSize) { rand.nextBoolean() }
//        val predicate:(Int)-> Boolean = { q ->
//            bools.getOrElse(q-low) {
//                fail("Binary search queried $q although it is outside range $rangeSize")
//            }    }
//}
//
//private fun exponentiallyAscendingInts(start:Int, coef: Double) = buildList <Int>{
//    require(start>0)
//    require(coef>1.0)
//    var current = start.toDouble()
//    val threshold = Int.MAX_VALUE.toDouble()
//    while (current<threshold) {
//        add(current.roundToInt())
//        current *= coef
//    }
//}
//
//
