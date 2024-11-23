package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.modular.*

@TeleOp(name = "Drive Train")
// @Disabled
class DriveTrain : BaseLinearOpMode() {
    private var power = ToggleableState(1, 0.25)
    private lateinit var gp1: GamepadState
   // private var powerModes = arrayOf(1.00, 0.66, 0.33)
    //private var powerModeIndex = 0



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
/*          //Left Bumper: cycle powerMode down
            if(gp1.current.left_bumper)
            {
                powerModeIndex++
            }
            if(gp1.current.right_bumper)
            {
                powerModeIndex--
            }
            if(powerModeIndex > powerModes.size -1)// prevents out of bounds errors
            {
                powerModeIndex = 0
            }

 */

            val motorPower = arrayOf(
                this.gp1.current.left_stick_x - this.gp1.current.left_stick_y + this.gp1.current.right_stick_x,
                -this.gp1.current.left_stick_x - this.gp1.current.left_stick_y - this.gp1.current.right_stick_x,
                -this.gp1.current.left_stick_x - this.gp1.current.left_stick_y + this.gp1.current.right_stick_x,
                this.gp1.current.left_stick_x - this.gp1.current.left_stick_y - this.gp1.current.right_stick_x,
            )



            toggleButtonMap.forEach { it.key.ifIsToggled(it.value) }

            this.allMotors.forEachIndexed { i, m ->
                m.power = (motorPower[i] * this.power.value).coerceIn(-this.power.value, this.power.value)
            }
        }
    }
}