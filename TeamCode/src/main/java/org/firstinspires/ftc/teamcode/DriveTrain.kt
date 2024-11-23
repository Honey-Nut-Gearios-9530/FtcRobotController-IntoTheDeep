package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.modular.*

@TeleOp(name = "DriveTrain")

class DriveTrain : BaseLinearOpMode() {
    private var power = ToggleableState(1., 0.25)
    private lateinit var gp1: GamepadState


    override fun runOpMode() {/* Initialization */
        gp1 = GamepadState(gamepad1)

        val toggleButtonMap = mapOf(
            GamepadButton(gp1, Gamepad::a) to power::toggle
        )

        this.initDriveTrain()

        /* End Initialization */
        this.waitForStart()

        while (this.opModeIsActive()) {
            this.gp1.cycle()

            val motorPower = arrayOf(
               // this.gp1.current.left_stick_x - this.gp1.current.left_stick_y + this.gp1.current.right_stick_x,
               // -this.gp1.current.left_stick_x - this.gp1.current.left_stick_y - this.gp1.current.right_stick_x,
                //-this.gp1.current.left_stick_x - this.gp1.current.left_stick_y + this.gp1.current.right_stick_x,
                //this.gp1.current.left_stick_x - this.gp1.current.left_stick_y - this.gp1.current.right_stick_x,

                this.gp1.current.left_stick_x - this.gp1.current.left_stick_y - this.gp1.current.left_trigger + this.gp1.current.right_trigger,
                -this.gp1.current.left_stick_x - this.gp1.current.left_stick_y + this.gp1.current.left_trigger - this.gp1.current.right_trigger,
                -this.gp1.current.left_stick_x - this.gp1.current.left_stick_y -this.gp1.current.left_trigger + this.gp1.current.right_trigger,
                this.gp1.current.left_stick_x - this.gp1.current.left_stick_y +  this.gp1.current.left_trigger - this.gp1.current.right_trigger,
            )

            toggleButtonMap.forEach { it.key.ifIsToggled(it.value) }

            this.allMotors.forEachIndexed { i, m ->
                m.power = (motorPower[i] * this.power.value).coerceIn(-this.power.value, this.power.value)
            }
        }
    }
}