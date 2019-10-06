package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boid(var p: INDArray, var v: INDArray) {
    fun act() {
        p = p.add(v)
        if(p.getDouble(0) >= 200) {
            p.putScalar(0, 0)
        }
    }
}

class Boids() {
    var boid = Boid(Nd4j.create(doubleArrayOf(0.0, 100.0)), Nd4j.create(doubleArrayOf(1.0, 0.0)))

    fun run() {
        boid.act()
    }

    fun getData(): FloatArray {
        return floatArrayOf(boid.p.getFloat(0), boid.p.getFloat(1))
    }
}