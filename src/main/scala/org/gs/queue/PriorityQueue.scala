/*
 * @http://algs4.cs.princeton.edu/24pq/MaxPQ.java.html
 * @see http://algs4.cs.princeton.edu/24pq/MinPQ.java.html
 */
package org.gs.queue

import collection.mutable.ArrayBuffer
import math.Ordering
import scala.annotation.tailrec

/**

 * @author garystruthers
 *
 * @param <T>
 */
class PriorityQueue[T](pq: ArrayBuffer[T]) {
  pq.append(null.asInstanceOf[T]) // don't use index 0
  private var n = 0
  def isEmpty(): Boolean = n == 0
  def less(a: Int, b: Int)(implicit ord: Ordering[T]) = ord.lt(pq(a), pq(b))
  def greater(a: Int, b: Int)(implicit ord: Ordering[T]) = ord.gt(pq(a), pq(b))

  private def exchange(child: Int, parent: Int) {
    val parentVal = pq(parent)
    pq.update(parent, pq(child))
    pq.update(child, parentVal)
  }

  private def swim(k: Int, cmp: (Int, Int) => Boolean) {
    @tailrec
    def loop(i: Int, j: Int) {
      if (i > 1 && cmp(j, i)) {
        exchange(i, j)
        loop(j, j / (2))
      }
    }
    loop(k, k./(2))
  }

  private def sink(k: Int, cmp: (Int, Int) => Boolean) {
    @tailrec
    def loop(k: Int, k2: Int): Unit = {
      if (k2 < n) {
        val k2Var = if (cmp(k2, k2 + 1)) k2 + 1 else k2
        val kVars = if (cmp(k, k2Var)) {
          exchange(k, k2Var)
          (k2Var,k2Var.*(2))
        } else (k, k2Var)
        loop(kVars._1, kVars._2)
      }
    }
    loop(k, k.*(2))
  }

  def insert(key: T, cmp: (Int, Int) => Boolean): Unit = { // Cost at most 1 + lg N compares
    pq.append(key)
    n = n + 1
    swim(n, cmp)
  }

  def pop(cmp: (Int, Int) => Boolean): Option[T] = {
    if (isEmpty) None else {
      exchange(1, n)
      val lastItem = pq.remove(n)
      n = n - 1
      sink(1, cmp)
      Some(lastItem)
    }
  }
}


class MinPQ[T](pq: ArrayBuffer[T]) extends PriorityQueue(pq) {
  def insert(key: T)(implicit ord: Ordering[T]): Unit = { // Cost at most 1 + lg N compares
    insert(key, greater)
  }
  def pop()(implicit ord: Ordering[T]): Option[T] = {
    pop(greater)
  }
}

class MaxPQ[T](pq: ArrayBuffer[T]) extends PriorityQueue(pq) {
  def insert(key: T)(implicit ord: Ordering[T]): Unit = { // Cost at most 1 + lg N compares
    insert(key, less)
  }
  def pop()(implicit ord: Ordering[T]): Option[T] = {
    pop(less)
  }
}

object PriorityQueue {

}