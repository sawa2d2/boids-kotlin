package boids

import kotlin.math.*
import kotlin.random.*
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.linear.ArrayRealVector

class Model(var N: Int) {
    val rangeMin = doubleArrayOf(0.0, 0.0)
    val rangeMax = doubleArrayOf(600.0, 600.0)
    val vMax = 2.5
    val boids = Array(N, {
        Boid(
            p = ArrayRealVector(doubleArrayOf(Random.nextDouble(600.0), Random.nextDouble(600.0))),
            v = ArrayRealVector(doubleArrayOf(Random.nextDouble(2 * vMax) - vMax, Random.nextDouble(2 * vMax) - vMax)),
            rangeMin = rangeMin,
            rangeMax = rangeMax,
            dis = 600.0,
            fov = PI * 2,
            vMax = vMax,
            cCoh = 0.1,
            cAlg = 0.5,
            cSep = 15.0
        )
    })

    fun run() {
        //observation phase
        for(boid in boids) {
            boid.observe(boids)
        }
        
        var pG = ArrayRealVector(2)
        var vG = ArrayRealVector(2)

        for(boid in boids) {
            pG = pG.add(boid.p)
            vG = vG.add(boid.v)
        }
        pG.mapDivideToSelf(boids.size.toDouble())
        vG.mapDivideToSelf(boids.size.toDouble())

        //decision phase
        for(boid in boids) {
            boid.decide(pG = pG, vG = vG)
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