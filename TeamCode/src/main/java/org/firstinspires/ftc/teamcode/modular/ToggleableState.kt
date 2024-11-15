package org.firstinspires.ftc.teamcode.modular

class ToggleableState<T>(
    private val original: T, private val new: T, private var flipped: Boolean = false
) {
    fun toggle() {
        flipped = !flipped
    }

    val value: T
        get() = if (flipped) new else original
}