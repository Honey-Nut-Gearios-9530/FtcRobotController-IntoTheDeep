package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import org.firstinspires.ftc.teamcode.modular.*

@TeleOp(name = "DriveTrain")
// @Disabled
class DriveTrain : BaseLinearOpMode() {
    private var power = ToggleableState(1.0, 0.25)
    private lateinit var gp1: GamepadState

    override fun runOpMode() {/* Initialization */
        gp1 = GamepadState(gamepad1)

        val toggleButtonMap = mapOf(
            GamepadButton(gp1, "a") to power::toggle
        )

        initDriveTrain()

        /* End Initialization */
        waitForStart()

        while (opModeIsActive()) {
            gp1.cycle()

            val motorPower = arrayOf(
                gp1.current.left_stick_x - gp1.current.left_stick_y + gp1.current.right_stick_x,
                -gp1.current.left_stick_x - gp1.current.left_stick_y - gp1.current.right_stick_x,
                -gp1.current.left_stick_x - gp1.current.left_stick_y + gp1.current.right_stick_x,
                gp1.current.left_stick_x - gp1.current.left_stick_y - gp1.current.right_stick_x,
            )

            toggleButtonMap.forEach { it.key.ifIsToggled(it.value) }

            allMotors.forEachIndexed { i, m ->
                m.power = (motorPower[i] * power.value).coerceIn(-power.value, power.value)
            }
        }
    }
}