import scala.collection.immutable.List


def hasDivisorLessThanOrEqualTo(x: Int, y:Int): Boolean = {
    if (y == 1) false
    else {
        if (x % y == 0) true
        else hasDivisorLessThanOrEqualTo(x, y-1)
    }
}
def isPrime(x: Int): Boolean = x match {
    case 1 => false
    case _ => ! hasDivisorLessThanOrEqualTo(x, x-1)
}

def isPalindrome[A](xs: List[A]): Boolean = xs match {
    case List() => true
    case List(value:A) => true
    case _ => (xs.head == xs.last) && isPalindrome(xs.tail.init)

}

def digitList(x:Int):List[Int] =  (x/10) match{
    case 0 => List(x)
    case _ => List(x%10) ::: digitList(x/10)
    //if (x/10 > 0) List(x%10) ::: digitList(x/10)
    //else List(x)
}

def primePalindrome(x:Int) : Boolean = isPrime(x) match {
    case true => isPalindrome(digitList(x))
    case false => false
    //if (! isPrime(x)) false
    //else isPalindrome(digitList(x))
}