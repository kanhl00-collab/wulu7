sealed trait Stream[+A] // Stream is covariant (marked by the +)
case object SNil extends Stream[Nothing] // The singleton Nil object
case class Cons[A](a: A, f: Unit => Stream[A]) extends Stream[A]

def take[A](n: Int, s: => Stream[A]): List[A] = s match {
    case SNil => Nil
    case Cons(a,f) => n match {
        case n if n > 0 => a :: take(n-1,f())
        case _ => Nil
    }
}

def filter[A](p: A => Boolean,s: => Stream[A]): Stream[A] = s match {
    case SNil => SNil
    case Cons(a,f) => p(a) match {
        case true => Cons(a, _ => filter(p,f()))
        case _ => filter(p,f())
    }
}

def zip[A,B](s: => Stream[A], t: => Stream[B]): Stream[(A,B)] = (s,t) match {
    case (SNil,_) => SNil
    case (_,SNil) => SNil
    case (Cons(a, f), Cons(b,g)) => Cons((a,b), _ => zip(f(),g()))
}

def merge[A](s: => Stream[A], t: => Stream[A]): Stream[A] = (s,t) match {
    case (SNil,_) => SNil
    case (_,SNil) => SNil
    case (Cons(a, f), Cons(b,g)) => Cons(a, _ => Cons(b,_ => merge(f(),g())))
}

def all[A](p: A => Boolean,s: => Stream[A]): Boolean = s match {
    case SNil => false
    case Cons(a,f) => p(a) match {
        case true => true && all(p,f())
        case _ => false
    }
}

def exists[A](p: A => Boolean,s: => Stream[A]): Boolean = s match {
    case SNil => false
    case Cons(a,f) => p(a) match {
        case true => true
        case _ => false || exists(p,f())
    }
}