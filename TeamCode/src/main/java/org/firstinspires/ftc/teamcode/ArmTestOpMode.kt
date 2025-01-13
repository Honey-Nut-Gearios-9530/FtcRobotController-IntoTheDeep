package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.GamepadState

@TeleOp(name = "Arm Test")
class ArmTestOpMode : LinearOpMode() {
    override fun runOpMode() {
        var motor = this.hardwareMap.dcMotor["arm"]
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        var switch = this.hardwareMap.touchSensor[""]
        var gp1 = GamepadState(this.gamepad1)
        var up = GamepadButton(gp1, Gamepad::dpad_up)
        var down = GamepadButton(gp1, Gamepad::dpad_down)
        this.waitForStart()
        while (this.opModeIsActive()) {
            gp1.cycle()                       
            if (switch.isPressed) {
                if (up.isPressed && !down.isPressed) {
                    motor.power = 0.5

                } else if (down.isPressed && !up.isPressed) {
                    motor.power = -0.5
                }
            }
        }
    }
}
