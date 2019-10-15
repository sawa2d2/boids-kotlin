package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Model(var N: Int) {
    val rangeMin = doubleArrayOf(0.0, 0.0)
    val rangeMax = doubleArrayOf(600.0, 600.0)
    val boids = Array(N, {
        Boid(
            p = Nd4j.rand(1, 2).mul(600.0),
            v = Nd4j.rand(1, 2).sub(0.5).muli(5 * 2.0),
            rangeMin = rangeMin,
            rangeMax = rangeMax
        )
    })

    fun run() {
        //observation phase
        for(boid in boids) {
            boid.observe(boids)
        }
        
        var pAve = Nd4j.zeros(2)
        var vAve = Nd4j.zeros(2)

        for(boid in boids) {
            pAve.addi(boid.p)
            vAve.addi(boid.v)
        }
        pAve.divi(boids.size)
        vAve.divi(boids.size)

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
            floatArrayOf(boids[it].p.getFloat(0), boids[it].p.getFloat(1))
        })
    }
}