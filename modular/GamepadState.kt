package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.Gamepad

class GamepadState(private val gamepad: Gamepad) {
    val current = Gamepad()
    val past = Gamepad()

    fun cycle() {
        this.past.copy(this.current)
        this.current.copy(this.gamepad)
    }
}