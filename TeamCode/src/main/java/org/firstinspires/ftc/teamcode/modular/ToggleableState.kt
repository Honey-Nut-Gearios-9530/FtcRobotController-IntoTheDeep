package org.firstinspires.ftc.teamcode.modular

class ToggleableState<T>(
    private val original: T, private val new: T, private var flipped: Boolean = false
) {
    fun toggle() {
        this.flipped = !this.flipped
    }

    val value: T
        get() = if (this.flipped) new else this.original
}