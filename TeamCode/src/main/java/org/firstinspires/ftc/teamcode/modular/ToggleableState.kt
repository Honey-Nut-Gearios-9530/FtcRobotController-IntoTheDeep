package org.firstinspires.ftc.teamcode.modular

class ToggleableState<T>(
    private var idx: Int = 0, private vararg val values: T
) {

    init {
        assert(idx in values.indices)
    }

    fun toggle() = right()

    fun left() {
        // negative mod is weird with jvm languages,
        // avoid the problem by making the number always positive
        // idx = (idx - 1 + values.size) % values.size
        idx = (idx - 1).coerceIn(values.indices)
    }

    fun right() {
        // idx = (idx + 1) % values.size
        idx = (idx + 1).coerceIn(values.indices)
    }

    val value: T
        get() = values[idx]
}
