package boids

import kotlin.math.*
import kotlin.random.*
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.linear.ArrayRealVector

class Model(var N: Int) {
    val rangeMin = doubleArrayOf(0.0, 0.0)
    val rangeMax = doubleArrayOf(600.0, 600.0)
    val vMax = 5.0
    val boids = Array(N, {
        Boid(
            p = ArrayRealVector(doubleArrayOf(Random.nextDouble(600.0), Random.nextDouble(600.0))),
            v = ArrayRealVector(doubleArrayOf(Random.nextDouble(2 * vMax) - vMax, Random.nextDouble(2 * vMax) - vMax)),
            rangeMin = rangeMin,
            rangeMax = rangeMax,
            vMax = vMax,
            cCoh = 0.1,
            cAlg = 0.5,
            cSep = 5.0
        )
    })

    fun run() {
        //observation phase
        for(boid in boids) {
            boid.observe(boids)
        }
        
        var pAve = ArrayRealVector(2)
        var vAve = ArrayRealVector(2)

        for(boid in boids) {
            pAve = pAve.add(boid.p)
            pAve = pAve.add(boid.v)
        }
        pAve.mapDivideToSelf(boids.size.toDouble())
        vAve.mapDivideToSelf(boids.size.toDouble())

        //decision phase
        for(boid in boids) {
            boid.decide(pAve = pAve, vAve = vAve)
        }

        //action phase
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