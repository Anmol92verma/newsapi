package com.tfexample.newsapisample

import android.util.Log

object WaterProblem {
  /**
   *  Constraints 1 ≤ n ≤ 1000
   *              1 ≤ used[i] ≤ 1000
   *              1 ≤ total[i] ≤ 1000
   * @param used array of integers where used[i] denotes the amount of water in iTH can
   * @param total array of integers where total[i] denotes capacity of the iTH can
   *  @return Min num of cans needed to contain all the water
   */
  fun minCan(used: IntArray, total: IntArray): Int {
    if (used.size in 1..1000 && total.size in 1..1000) {
      var quantity = used.sum()
      val waterCans = total.map { WaterCan(it) }.sortedBy { it.capacity }.reversed()
      var waterIndex = 0
      var usedIndex = 0
      while (quantity != 0) {
        try {
          // fill water can and adjust quantity until quantity is not 0
          val fillable = Math.min(used[usedIndex], quantity)
          waterCans[waterIndex].fill(fillable)
          quantity -= fillable
          usedIndex++
        } catch (e: OverfillException) {
          // when a water can is overfilled move to next water can
          waterIndex++
        }
        if (waterCans.size == waterIndex.plus(1)) {
          break
        }
      }
      Log.e("Required cans ${waterIndex.plus(1)}", waterCans.toString())
      return waterIndex.plus(1)
    }
    return 0
  }
}

data class WaterCan(val capacity: Int, var filled: Int = 0) {
  fun fill(amount: Int) {
    if (amount.plus(filled) <= capacity) {
      this.filled += amount
    } else {
      throw OverfillException()
    }
  }
}

class OverfillException : Throwable()
