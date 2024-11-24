package org.firstinspires.ftc.teamcode.modular

import kotlin.reflect.KCallable

@Suppress("MemberVisibilityCanBePrivate")
class GamepadButton(private val gamepad: GamepadState, private val button: KCallable<Boolean>) {

    val isToggled: Boolean
        get() = this.button.call(this.gamepad.current) && !this.button.call(this.gamepad.past)

    val isPressed: Boolean
        get() = this.button.call(this.gamepad.current)

    inline fun ifIsToggled(block: () -> Unit) {
        if (this.isToggled) block()
    }

    inline fun ifIsPressed(block: () -> Unit) {
        if (this.isPressed) block()
    }
}