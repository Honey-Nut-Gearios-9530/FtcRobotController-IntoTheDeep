package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.modular.BaseLinearOpMode
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.GamepadState
import org.firstinspires.ftc.teamcode.modular.ToggleableState
import kotlin.math.abs

@TeleOp(name = "Drive Train")

class DriveTrain : BaseLinearOpMode() {
    // kotlin does not do numeric type promotion, if the 3rd arg is just "1" than T cannot be inferred
    private var power = ToggleableState(2, 0.33, 0.67, 1.0)

    private lateinit var gp1: GamepadState
    private var leftTurn = this.gamepad1.left_trigger
    private var rightTurn = this.gamepad1.right_trigger
    private var forwardMotion = this.gamepad1.left_stick_y
    private var crabMotion = this.gamepad1.left_stick_x
    private var amr = 0 // no idea

    private fun update(){
        leftTurn = this.gp1.current.left_trigger
        rightTurn = this.gp1.current.right_trigger
        forwardMotion = this.gp1.current.left_stick_y
        crabMotion = this.gp1.current.left_stick_x
    }


    override fun runOpMode() {/* Initialization */
        telemetry.msTransmissionInterval = 100



        val toggleButtonMap = mapOf(
            GamepadButton(GamepadState(gamepad1), Gamepad::left_bumper) to power::left,
            GamepadButton(GamepadState(gamepad1), Gamepad::right_bumper) to power::right
        )

        this.initDriveTrain()

        /* End Initialization */
        this.waitForStart()

        while (this.opModeIsActive()) {
            //this.gp1.cycle()
            update()

            toggleButtonMap.forEach { it.key.ifIsToggled(it.value) }

            /* Calculates motor power in accordance with the allMotors array
               and formulas found here: https://github.com/brandon-gong/ftc-mecanum
             */
            val turnPower = -rightTurn + leftTurn

            val motorPower = arrayOf(
                crabMotion - forwardMotion - turnPower,
                -crabMotion - forwardMotion + turnPower,
                -crabMotion - forwardMotion - turnPower,
                crabMotion - forwardMotion + turnPower,
            )

            // Magnitude of the maximum value, not velocity
            val max = abs(motorPower.maxBy { abs(it) })

            // Normalize if greater the max is greater than 1
            if (max > 1) motorPower.forEachIndexed { i, _ -> motorPower[i] /= max }

            // Update the motors with the proper power
            this.allMotors.forEachIndexed { i, m ->
                m.power = motorPower[i].toDouble() * power.value
            }

            telemetry.update()
        }

    }
}
