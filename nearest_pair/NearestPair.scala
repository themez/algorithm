import scala.util.Random

/**
  * Created with IntelliJ IDEA.
  * User: xian.zeng
  * Date: 6/24/2014
  * Time: 10:02 AM
  */
object NearestPair extends App{

   case class Dot(x: Int, y: Int)

   def genDots: Seq[Dot] = {
     def dotGenerator:Stream[Dot] = Stream.cons(Dot(Random.nextInt(100), Random.nextInt(100)), dotGenerator)
     def _genDots(dots: Set[Dot]): Set[Dot] = {
       if (dots.size == 100) dots else _genDots(dots + dotGenerator.head)
     }
     _genDots(Set()).toSeq
   }


   def distanceSquare(p1: Dot, p2: Dot):Int = (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)

   def distanceSquare(p: Pair[Dot, Dot]):Int = distanceSquare(p._1, p._2)

  def merge(a1: (Seq[Dot], Pair[Dot, Dot], Int), a2: (Seq[Dot], Pair[Dot, Dot], Int), vLine: Int): (Seq[Dot], Pair[Dot, Dot], Int) = {
    val minDis = Math.min(a1._3, a2._3)
    val centralPairs = for {
      p1 <- a1._1.filter(_.x >= vLine - minDis)
      p2 <- a2._1.filter(_.x <= vLine + minDis)
    } yield Pair(p1, p2)

    val centralMinPair = if (centralPairs.isEmpty) null else centralPairs.minBy(distanceSquare)

    val minPair = Seq(Tuple2(centralMinPair, if (centralMinPair == null) Int.MaxValue else distanceSquare(centralMinPair)), Tuple2(a1._2, a1._3), Tuple2(a2._2, a2._3)).minBy(_._2)

    Tuple3(a1._1 ++ a2._1, minPair._1, minPair._2)
  }

   def findNearest2(dots: Seq[Dot]):(Seq[Dot], Pair[Dot, Dot], Int) = {
     if (dots.size == 2) Tuple3(dots, Pair(dots.head, dots.last), distanceSquare(dots.head, dots.last))
     else if (dots.size < 2) Tuple3(dots, null, Int.MaxValue)
     else {
       val centralX = (dots.head.x + dots.last.x)/2
       val leftPar = dots.filter(_.x<=centralX)
       val rightPar = dots.filter(_.x>centralX)
       if(rightPar.size != 0)
        merge(findNearest2(leftPar), findNearest2(rightPar), centralX)
       else{
         val minPair = (for {
           p1 <- leftPar
           p2 <- leftPar
           if p1 != p2
         } yield Pair(p1, p2)).minBy(distanceSquare)
         Tuple3(dots, minPair, distanceSquare(minPair))
       }
     }
   }


   override def main(args: Array[String]){
     val dots = genDots.sortBy(_.x)

     val nearest = findNearest2(dots)
     println(nearest._2, nearest._3)
   }
 }
