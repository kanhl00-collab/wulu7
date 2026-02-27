import scala.collection.immutable.List


sealed trait LeafTree[A]
case class Branch[A](left: LeafTree[A], right: LeafTree[A]) extends LeafTree[A]
case class Leaf[A](value: A) extends LeafTree[A]


sealed trait BinTree[A]
case class Node[A](left: BinTree[A], parent: A, right: BinTree[A]) extends BinTree[A]
case class Empty[A]() extends BinTree[A]


def flatten[A](t : LeafTree[A]): List[A] = t match {
    case Leaf(value) => List(value);
    case Branch(left, right) => flatten(left) ::: flatten(right);
}

def flatten[A](t : BinTree[A]): List[A] = t match {
    case Node(left, parent, right) => flatten(left) ::: List(parent) ::: flatten(right)
    case Empty() => List()
}

def merge(xs : List[Int], ys : List[Int]) : List[Int] = (xs, ys) match {
    case (List(), _) => ys
    case (_, List()) => xs
    case (_, _) => if (xs.head < ys.head) xs.head :: merge(xs.tail, ys) else ys.head :: merge(xs, ys.tail)
}

def orderedElems(t : LeafTree[Int]): List[Int] = t match {
    case Leaf(value) => List(value)
    case Branch(left, right) => merge(orderedElems(left), orderedElems(right))
}

def orderedElems(t : BinTree[Int]): List[Int] = t match {
    case Empty() => List()
    case Node(left, parent, right) => merge(merge(orderedElems(left), List(parent)), orderedElems(right))
}

sealed trait StructTree[A, B]
case class Parent[A, B](left: StructTree[A, B], parent: A, right: StructTree[A, B]) extends StructTree[A, B]
case class Child[A, B](value: B) extends StructTree[A, B]

