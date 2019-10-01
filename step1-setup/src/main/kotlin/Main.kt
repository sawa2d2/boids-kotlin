import processing.core.*
import boids.*

class Main : PApplet () {
    var d = 40F // diameter
    var model = Boids()

    override fun settings() {
        size(200, 200)
    }
    
    override fun draw(){
        background(255)
        val p = model.getData()
        ellipse(p[0], p[1], d, d)
        model.run()
    }
 
    fun run(args: Array<String>) : Unit = PApplet.main("Main")
}

fun main(args : Array<String>) : Unit = Main().run(args)