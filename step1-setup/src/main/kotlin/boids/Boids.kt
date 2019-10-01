package boids

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

class Boids() {
    var p = Nd4j.create(doubleArrayOf(0.0, 100.0))
    var v = Nd4j.create(doubleArrayOf(1.0, 0.0))
    
    fun run(){
        p = p.add(v)
        if(p.getDouble(0) >= 200) {
            p.putScalar(0, 0)
        } 
    }

    fun getData(): FloatArray {
        return floatArrayOf(p.getFloat(0), p.getFloat(1))
    }
}