package boids

import kotlin.math.*
import kotlin.random.*
import org.apache.commons.math3.linear.RealVector
import org.apache.commons.math3.linear.ArrayRealVector

class Boid(var p: RealVector, var v: RealVector) {
    fun act() {
        p = p.add(v)
        if(p.getEntry(0) >= 200.0) {
            p.setEntry(0, 0.0)
        }
    }
}

class Model() {
    var boid = Boid(ArrayRealVector(doubleArrayOf(0.0, 100.0)), ArrayRealVector(doubleArrayOf(1.0, 0.0)))

    fun run() {
        boid.act()
    }

   fun getData(): FloatArray {
        return floatArrayOf(
            boid.p.getEntry(0).toFloat(),
            boid.p.getEntry(1).toFloat()
        )
    }
}