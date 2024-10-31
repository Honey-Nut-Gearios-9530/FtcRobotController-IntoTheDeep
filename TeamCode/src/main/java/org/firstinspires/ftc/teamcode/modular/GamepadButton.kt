package org.firstinspires.ftc.teamcode.modular

class GamepadButton(val button: () -> Boolean) {
    private var prevValue = false

    val value: Boolean
        get() = button()

    val canPress: Boolean
        get() {
            val currentVal = value
            if (currentVal != prevValue) {
                prevValue = currentVal

                return true
            }

            return false
        }
}