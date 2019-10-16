package boids

import kotlin.math.*
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.linear.ArrayRealVector

public class Boid(
    var p: RealVector,
    var v: RealVector,
    val rangeMin: DoubleArray,
    val rangeMax: DoubleArray,
    val vMax: Double,
    val dis : Double,
    val fov : Double,
    val cCoh: Double,
    val cAlg: Double,
    val cSep: Double
    ) {
    var neighbors = mutableListOf(this)
    //var neighbors = arrayOf(this)
    var vNext = ArrayRealVector()

    fun observe(boids: Array<Boid>) {
        //this.neighbors = boids
        this.neighbors = mutableListOf<Boid>()
        for(boid in boids) {
            if(p == boid.p){
                continue
            }
            val relativeP = p.subtract(boid.p)
            val cosine = relativeP.cosine(boid.v)
            val nrm = relativeP.getNorm()
            if(nrm != 0.0) {
                if(isInScope(distance = nrm, cosine = cosine)) {
                    neighbors.add(boid)
                }
            }
        }
    }

    private fun isInScope(distance: Double, cosine: Double) = (distance <= dis && cos(fov/2) <= cosine && cosine <= 1.0)

    fun decide(pG: RealVector, vG: RealVector) {
        var aCoh = pG.subtract(p)
        var aAlg = vG.subtract(v)
        var aSep = ArrayRealVector(2)
        
        for(neighbor in neighbors) {
            if(neighbor != this){
                val dir = p.subtract(neighbor.p)
                val nrm = dir.getNorm()
                if(nrm == 0.0) {
                    println("!!")
                    continue
                }
                val sep = dir.unitVector().mapDivide(nrm)
                aSep = aSep.add(sep)
            }
        }

        aCoh = aCoh.mapMultiply(cCoh)
        aAlg = aAlg.mapMultiply(cAlg)
        aSep.mapMultiplyToSelf(cSep)
        var a = ArrayRealVector(2)
        a = a.add(aCoh)
        a = a.add(aAlg)
        a = a.add(aSep)

        var vNext = a.add(v)
        //fixV(vNext, vMax)
        //vNext.mapMultiplyToSelf(min((vMax/vNext.getNorm()), 1.0))
        vNext.mapMultiplyToSelf(vMax/(if(vNext.getNorm() != 0.0) vNext.getNorm() else 1.0))
        this.vNext = vNext
    }

    fun act() {
        v = vNext
        p = p.add(vNext)
        fixP(p)
    }

    //private fun fixV(v: RealVector, vMax: Double) = v.mapMultiplyToSelf(min(vMax/v.getNorm(), 1.0))
    
    private fun fixP(p: RealVector) {
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
