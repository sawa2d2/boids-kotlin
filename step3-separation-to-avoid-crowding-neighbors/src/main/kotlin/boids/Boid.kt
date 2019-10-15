package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boid(
        var p: INDArray,
        var v: INDArray,
        val rangeMin: DoubleArray,
        val rangeMax: DoubleArray
    ) {
    var neighbors = arrayOf(this)
    var vNext = Nd4j.zeros(2);
    val cCoh = 0.1
    val cAlg = 0.5
    val cSep = 0.00045

    fun observe(boids: Array<Boid>) {
        neighbors = boids
    }

    fun decide(pAve: INDArray, vAve: INDArray) {
        val aCoh = pAve.sub(p)
        val aAlg = vAve.sub(v)
        var aSep = Nd4j.zeros(2)
    
        for(neighbor in neighbors) {
            if(neighbor != this){
                val u = p.sub(neighbor.p)
                //val d = u.norm2Number()
                aSep.addi(u/*.div(d)*/)
                //println(e)
            }
        }

        var a = Nd4j.zeros(2)
        a.add(aCoh.mul(cCoh))
        a.add(aAlg.mul(cAlg))
        a.add(aSep.mul(cSep))
        
        val vNext = v.add(a)

        //constriction the velocity
        val vMax: Number = 5.0
        //val vNorm = v.norm2Number()
        this.vNext = vNext//.div(vNorm).mul(vMax)
    }

    fun act() {
        v = vNext
        p = p.add(vNext)
        fix()
    }

    private fun fix() {
        for(d in 0..1) {
            if(p.getDouble(d) <= rangeMin[d]) {
               p.putScalar(d, rangeMin[d])
               v.putScalar(d, -v.getDouble(d))
            }
            if(p.getDouble(d) >= rangeMax[d]) {
               p.putScalar(d, rangeMax[d])
               v.putScalar(d, -v.getDouble(d))
            }
        }
    }
}
