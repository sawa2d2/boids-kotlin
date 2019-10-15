package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Model(var N: Int) {
    val rangeMin = Nd4j.create(doubleArrayOf(0.0, 0.0))
    val rangeMax = Nd4j.create(doubleArrayOf(600.0, 600.0))

    val boids = Array(N, {
        Boid(
            p = Nd4j.rand(1, 2).mul(600.0),
            v = Nd4j.rand(1, 2).sub(0.5).mul(5 * 2.0),
            rangeMin = rangeMin,
            rangeMax = rangeMax
        )
    })

    fun run() {
        var pAve = Nd4j.zeros(2)
        var vAve = Nd4j.zeros(2)

        for(i in boids) {
            pAve = pAve.add(i.p)
            vAve = vAve.add(i.v)
        }
        pAve = pAve.div(boids.size)
        vAve = vAve.div(boids.size)


        for(i in boids) {
            i.run(pAve = pAve, vAve = vAve, neighbors = boids)
        }
    }

    fun getData(): Array<FloatArray> {
        return Array<FloatArray>(boids.size, {
            floatArrayOf(boids[it].p.getFloat(0), boids[it].p.getFloat(1))
        })
    }
}