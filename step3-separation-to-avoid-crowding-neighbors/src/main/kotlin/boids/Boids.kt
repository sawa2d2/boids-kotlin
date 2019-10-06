package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boid(var p: INDArray, var v: INDArray, val rangeMin: INDArray, val rangeMax: INDArray) {
    var neighbors = arrayOf(this)

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

class Boids(var N: Int) {
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
        val cCoh = 0.05
        val cAlg = 0.5
        val cSep = 0.0001

        var pAve = Nd4j.zeros(2)
        var vAve = Nd4j.zeros(2)

        for(i in boids) {
            pAve = pAve.add(i.p)
            vAve = vAve.add(i.v)
        }
        pAve = pAve.div(boids.size)
        vAve = vAve.div(boids.size)

        for(i in boids) {
            val aCoh = pAve.sub(i.p)
            val aAlg = vAve.sub(i.v)
            var aSep = Nd4j.zeros(2)
            
            i.v = i.v.add(aCoh.mul(cCoh))
            i.v = i.v.add(aAlg.mul(cAlg))
            i.v = i.v.add(aSep.mul(cSep))
            
            i.act()
        }
    }

    fun getData(): Array<FloatArray> {
        return Array<FloatArray>(boids.size, {
            floatArrayOf(boids[it].p.getFloat(0), boids[it].p.getFloat(1))
        })
    }
}