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
    var vNext: RealVector = ArrayRealVector()

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
        var aCoh: RealVector = pG.subtract(p)
        var aAlg: RealVector = vG.subtract(v)
        var aSep: RealVector = ArrayRealVector(2)
        
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
        aSep = aSep.mapMultiplyToSelf(cSep)

        var a: RealVector = ArrayRealVector(2).add(aCoh).add(aAlg).add(aSep)
        var vNext: RealVector = a.add(v)
        vNext = fixV(vNext, vMax)
        
        this.vNext = vNext
    }

    fun act() {
        v = vNext
        p = p.add(vNext)
        p = fixP(p)
    }

    private fun fixP(p: RealVector): RealVector {
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
        return p
    }
    
    private fun fixV(v: RealVector, vMax: Double): RealVector
        = v.mapMultiplyToSelf(vMax/(if(v.getNorm() != 0.0) v.getNorm() else 1.0))
    //    = v.mapMultiplyToSelf(min(vMax/v.getNorm(), 1.0))
}
