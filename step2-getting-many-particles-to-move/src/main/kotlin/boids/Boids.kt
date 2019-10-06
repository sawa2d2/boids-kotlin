package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boid(var p: INDArray, var v: INDArray, val rangeMin: INDArray, val rangeMax: INDArray) {
    fun act() {
        p = p.add(v)
        fix()
    }

    fun fix() {
        for(d in 0..1) {
            if(p.getDouble(d) <= rangeMin.getDouble(d)) {
               p.putScalar(d, rangeMin.getDouble(d))
               v.putScalar(d, -v.getDouble(d))
            }
            if(p.getDouble(d) >= rangeMax.getDouble(d)) {
               p.putScalar(d, rangeMax.getDouble(d))
               v.putScalar(d, -v.getDouble(d))
            }
        }
    }
}

class Boids() {
    val rangeMin = Nd4j.create(doubleArrayOf(0.0, 0.0))
    val rangeMax = Nd4j.create(doubleArrayOf(600.0, 600.0))

    val boids = Array(100, {
        Boid(
            p = Nd4j.rand(2, 1).mul(600.0),
            v = Nd4j.rand(2, 1).sub(0.5).mul(5 * 2.0),
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
            floatArrayOf(boids[it].p.getFloat(0), boids[it].p.getFloat(1))
        })
    }
}