package boids

import kotlin.math.*
import kotlin.random.*
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.linear.ArrayRealVector

public class Boid(
    var p: RealVector,
    var v: RealVector,
    val rangeMin: DoubleArray,
    val rangeMax: DoubleArray
    ) {
    fun act() {
        p = p.add(v)
        fixP(p)
    }

    fun fixP(p: RealVector) {
        for(d in 0..1) {
            if(p.getEntry(d) < rangeMin[d]) {
               p.setEntry(d, rangeMin[d])
               v.setEntry(d, -v.getEntry(d))
            }
            if(p.getEntry(d) > rangeMax[d]) {
               p.setEntry(d, rangeMax[d])
               v.setEntry(d, -v.getEntry(d))
            }
        }
    }
}

class Boids() {
    val rangeMin = doubleArrayOf(0.0, 0.0)
    val rangeMax = doubleArrayOf(600.0, 600.0)
    val vMax = 2.5

    val boids = Array(100, {
        Boid(
            p = ArrayRealVector(doubleArrayOf(Random.nextDouble(600.0), Random.nextDouble(600.0))),
            v = ArrayRealVector(doubleArrayOf(Random.nextDouble(2 * vMax) - vMax, Random.nextDouble(2 * vMax) - vMax)),
            rangeMin = rangeMin,
            rangeMax = rangeMax
        )
    })

    fun run() {
        for(boid in boids) {
            boid.act()
        }
    }


    fun getData(): Array<FloatArray> {
        return Array<FloatArray>(boids.size, {
            floatArrayOf(
                boids[it].p.getEntry(0).toFloat(),
                boids[it].p.getEntry(1).toFloat()
            )
        })
    }
}