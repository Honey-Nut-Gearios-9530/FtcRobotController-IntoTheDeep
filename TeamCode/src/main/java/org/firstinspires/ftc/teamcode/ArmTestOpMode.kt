package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.modular.BaseLinearOpMode
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.GamepadState

@Suppress("unused")
@TeleOp(name = "Arm Test")
class ArmTestOpMode : BaseLinearOpMode() {
    override fun runOpMode() {
        this.telemetry.msTransmissionInterval = 100
        this.initDriveTrain()
        var arm = this.hardwareMap.dcMotor["arm"]
        var elevator = this.hardwareMap.dcMotor["elevator"]
        arm.direction = DcMotorSimple.Direction.REVERSE
        arm.mode = DcMotor.RunMode.RUN_USING_ENCODER
        elevator.mode = DcMotor.RunMode.RUN_USING_ENCODER
        var switch = this.hardwareMap.touchSensor["touch.sensor"]
        var lastSwitch = switch.isPressed
        var gp1 = GamepadState(this.gamepad1)
        var up = GamepadButton(gp1, Gamepad::dpad_up)
        var down = GamepadButton(gp1, Gamepad::dpad_down)
        this.waitForStart()
        while (this.opModeIsActive()) {
            gp1.cycle()
            val currSwitch = switch.isPressed
            if (currSwitch && !lastSwitch) {
                arm.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                arm.mode = DcMotor.RunMode.RUN_USING_ENCODER
            }
            if (up.isPressed && !down.isPressed && arm.currentPosition < 7000) arm.power = 0.2
            else if (down.isPressed && !up.isPressed && !currSwitch) arm.power = -0.2
            else arm.power = 0.0
            elevator.power = -gp1.current.right_stick_y.toDouble() * 0.2
            lastSwitch = currSwitch

            this.telemetry.addData("arm pos", arm.currentPosition)

            /*
              gets the current Position (x & y in mm, and heading in degrees) of the robot, and prints it.
            */
            val pos = odometry.position
            val data = "{X: %.3f, Y: %.3f, H: %.3f}".format(
                pos.getX(DistanceUnit.MM),
                pos.getY(DistanceUnit.MM),
                pos.getHeading(AngleUnit.DEGREES)
            )
            this.telemetry.addData("Position", data)

            /*
            gets the current Velocity (x & y in mm/sec and heading in degrees/sec) and prints it.
             */
            val vel = odometry.velocity
            val velocity = "{XVel: %.3f, YVel: %.3f, HVel: %.3f}".format(
                vel.getX(DistanceUnit.MM),
                vel.getY(DistanceUnit.MM),
                vel.getHeading(AngleUnit.DEGREES)
            )
            this.telemetry.addData("Velocity", velocity)

            /*
            Gets the Pinpoint device status. Pinpoint can reflect a few states. But we'll primarily see
            READY: the device is working as normal
            CALIBRATING: the device is calibrating and outputs are put on hold
            NOT_READY: the device is resetting from scratch. This should only happen after a power-cycle
            FAULT_NO_PODS_DETECTED - the device does not detect any pods plugged in
            FAULT_X_POD_NOT_DETECTED - The device does not detect an X pod plugged in
            FAULT_Y_POD_NOT_DETECTED - The device does not detect a Y pod plugged in
            */
            this.telemetry.addData("Status", odometry.deviceStatus)

            //prints/gets the current refresh rate of the Pinpoint
            this.telemetry.addData("Pinpoint Frequency", odometry.frequency)
            this.telemetry.update()
        }
    }
}
