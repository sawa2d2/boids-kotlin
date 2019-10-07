package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boid(var p: INDArray, var v: INDArray, val rangeMin: INDArray, val rangeMax: INDArray) {
    var neighbors = arrayOf(this)
    val cCoh = 0.1
    val cAlg = 0.5
    val cSep = 0.1

    fun run(pAve: INDArray, vAve: INDArray, neighbors: Array<Boid>) {
        val aCoh = pAve.sub(p)
        val aAlg = vAve.sub(v)
        var aSep = Nd4j.create(doubleArrayOf(0.0, 0.0))
    
        for(j in neighbors) {
            if(j == this){
                continue
            }
            val u = p.sub(j.p)
            val d = u.norm2Number()
            aSep = aSep.add(u.div(d))
            //println(e)
        }

        v = v.add(aCoh.mul(cCoh))
        v = v.add(aAlg.mul(cAlg))
        v = v.add(aSep.mul(cSep))

        val vMax: Number = 5.0
        val vNorm = v.norm2Number()
        v = v.div(vNorm).mul(vMax)

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