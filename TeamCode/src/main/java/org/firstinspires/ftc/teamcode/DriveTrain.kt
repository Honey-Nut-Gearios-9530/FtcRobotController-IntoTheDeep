package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.matrices.GeneralMatrixF
import org.firstinspires.ftc.teamcode.modular.BaseLinearOpMode
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.GamepadState
import org.firstinspires.ftc.teamcode.modular.ToggleableState

@TeleOp(name = "DriveTrain")
// @Disabled
class DriveTrain : BaseLinearOpMode() {
    private var power = ToggleableState(1.0, 0.25)
    private lateinit var gp1: GamepadState

    override fun runOpMode() {/* Initialization */
        telemetry.msTransmissionInterval = 100

        gp1 = GamepadState(gamepad1)

        val toggleButtonMap = mapOf(
            GamepadButton(gp1, Gamepad::left_bumper) to power::left,
            GamepadButton(gp1, Gamepad::right_bumper) to power::right
        )

        this.initDriveTrain()

        /* End Initialization */
        this.waitForStart()

        while (this.opModeIsActive()) {
            this.gp1.cycle()

            toggleButtonMap.forEach { it.key.ifIsToggled(it.value) }

            // matrix that holds the directions the wheels need to go
            // for movement in the x, y and rotational axes
            val directionMatrix = GeneralMatrixF(
                4, 3, floatArrayOf(
                    -1f, 1f, -1f,
                    1f, 1f, 1f,
                    1f, 1f, -1f,
                    -1f, 1f, 1f
                )
            )

            // matrix that hold the state of controller input that is translated to wheel speeds
            val inputMatrix = GeneralMatrixF(
                3, 1, floatArrayOf(
                    -this.gp1.current.left_stick_x,
                    -this.gp1.current.left_stick_y,
                    this.gp1.current.left_trigger + -this.gp1.current.right_trigger
                )
            )

            // telemetry.addData("multiplier", power.value)

            // telemetry.addData("left_trigger", this.gp1.current.left_trigger)

            // 4 length float that holds the speeds for each wheel
            // calculated by summing the weights of the wheel movement
            // for each axis via matrix multiplication
            val wheelSpeeds = (directionMatrix.multiplied(inputMatrix) as GeneralMatrixF).data

            // normalize the wheel speeds to within 0..1 and apply the new speeds
            val maxSpeed = wheelSpeeds.maxOfOrNull(Math::abs)!!
            // telemetry.addData("max speed", maxSpeed)
            val normalizedWheelSpeeds = wheelSpeeds.map { if (maxSpeed > 1) it / maxSpeed else it }
            // telemetry.addData("raw speeds", wheelSpeeds.map { String.format("%.2f", it) })
            // telemetry.addData("speeds", normalizedWheelSpeeds.map { String.format("%.2f", it) })
            normalizedWheelSpeeds.zip(this.allMotors)
                .forEach { it.second.power = it.first.toDouble() * power.value }

            telemetry.update()
        }

    }
}
