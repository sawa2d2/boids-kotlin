import processing.core.*
import boids.*

class Main : PApplet () {
    var d = 8F // diameter
    var model = Boids()

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
 
    fun run(args: Array<String>) : Unit = PApplet.main("Main")
}

fun main(args : Array<String>) : Unit = Main().run(args)