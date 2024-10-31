package org.firstinspires.ftc.teamcode.modular

class MutableState<T>(private val obj: T){
    var value = obj

    fun reset() {value = obj}
}