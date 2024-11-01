package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.Gamepad

class GamepadState(private val gamepad: Gamepad)
{
    val current = Gamepad()
    val past = Gamepad()

    fun cycle() {
        past.copy(current)
        current.copy(gamepad)
    }
}