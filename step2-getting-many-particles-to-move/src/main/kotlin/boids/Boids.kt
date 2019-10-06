package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boid(var p: INDArray, var v: INDArray) {
    fun act() {
        p = p.add(v)
    }
}

class Boids() {
    val boids = Array(100, {
        Boid(Nd4j.rand(2, 1).mul(600.0), Nd4j.rand(2, 1).sub(0.5).mul(5 * 2.0))
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