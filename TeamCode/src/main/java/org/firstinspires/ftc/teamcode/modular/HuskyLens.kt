package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.hardware.dfrobot.HuskyLens

abstract class HuskyLens : BaseLinearOpMode() {
    enum class LensMode {
        TAG_RECOGNITION, COLOR_RECOGNITION, OBJECT_RECOGNITION, OBJECT_CLASSIFICATION
    }

    fun init(mode: LensMode) {
        val huskyLens = hardwareMap.get(HuskyLens::class.java, "huskylens")
        require(huskyLens.knock()) {"Failed to communicate with HuskyLens"}
        huskyLens.initialize()
        when (mode) {
            LensMode.TAG_RECOGNITION -> huskyLens.selectAlgorithm(HuskyLens.Algorithm.TAG_RECOGNITION)
            LensMode.COLOR_RECOGNITION -> huskyLens.selectAlgorithm(HuskyLens.Algorithm.COLOR_RECOGNITION)
            LensMode.OBJECT_RECOGNITION -> huskyLens.selectAlgorithm(HuskyLens.Algorithm.OBJECT_RECOGNITION)
            LensMode.OBJECT_CLASSIFICATION -> huskyLens.selectAlgorithm(HuskyLens.Algorithm.OBJECT_CLASSIFICATION)
        }
    }

    fun readLens(mode: LensMode) : Array<HuskyLens.Block> {
        val huskyLens = hardwareMap.get(HuskyLens::class.java, "huskylens")
        val blocks: Array<HuskyLens.Block> = huskyLens.blocks()
        return blocks
    }
}