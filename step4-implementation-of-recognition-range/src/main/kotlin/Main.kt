import processing.core.*
import boids.*

class Main : PApplet () {
    var d = 8F // diameter
    var model = Model(N = 200)

    override fun settings() {
        size(600, 600)
    }
    
    override fun draw(){
        background(255)
        val particles = model.getData()
        for(p in particles) {
            ellipse(p[0], p[1], d, d)
        }
        model.run()
    }
 
    fun run() : Unit = PApplet.main("Main")
}

fun main(args : Array<String>) : Unit = Main().run()