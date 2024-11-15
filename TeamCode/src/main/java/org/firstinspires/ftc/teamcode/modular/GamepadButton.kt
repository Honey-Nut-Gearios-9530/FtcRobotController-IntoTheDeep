package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.hardware.Gamepad

class GamepadButton(private val gamepad: GamepadState, private val button: String) {
    private val buttonFunction = Gamepad::class.members.find { it.name == button }

    val isToggled: Boolean
        get() = buttonFunction!!.call(gamepad.current) as Boolean && !(buttonFunction.call(gamepad.past) as Boolean)

    val isPressed: Boolean
        get() = buttonFunction!!.call(gamepad.current) as Boolean

    fun ifIsToggled(block: () -> Unit) {
        if (isToggled) block()
    }

    fun ifIsPressed(block: () -> Unit) {
        if (isPressed) block()
    }
}