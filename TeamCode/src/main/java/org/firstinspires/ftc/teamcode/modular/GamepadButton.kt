package org.firstinspires.ftc.teamcode.modular

import kotlin.reflect.KCallable

class GamepadButton(private val gamepad: GamepadState, private val button: KCallable<*>) {

    val isToggled: Boolean
        get() = this.button.call(this.gamepad.current) as Boolean && !(this.button.call(this.gamepad.past) as Boolean)

    val isPressed: Boolean
        get() = this.button.call(this.gamepad.current) as Boolean

    fun ifIsToggled(block: () -> Unit) {
        if (this.isToggled) block()
    }

    fun ifIsPressed(block: () -> Unit) {
        if (this.isPressed) block()
    }
}