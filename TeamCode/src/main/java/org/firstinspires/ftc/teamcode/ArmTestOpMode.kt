package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.TouchSensor
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.teamcode.modular.BaseLinearOpMode
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.GamepadState
import kotlin.math.sign

@Suppress("unused")
@TeleOp(name = "Arm Test")
class ArmTestOpMode : BaseLinearOpMode() {
    override fun runOpMode() {
        this.telemetry.msTransmissionInterval = 100
        this.initDriveTrain()
        var arm = this.hardwareMap["arm"] as DcMotorEx
        arm.direction = DcMotorSimple.Direction.REVERSE
        arm.mode = DcMotor.RunMode.RUN_USING_ENCODER
        var elevator = this.hardwareMap["elevator"] as DcMotorEx
        elevator.direction = DcMotorSimple.Direction.REVERSE
        elevator.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        var ratchet = ServoWrapper(this.hardwareMap["ratchet"] as Servo, 0.1, 0.0)
        ratchet.inactivate()
        val leftSpinner = this.hardwareMap["leftSpinner"] as CRServo
        leftSpinner.direction = DcMotorSimple.Direction.REVERSE
        val rightSpinner = this.hardwareMap["rightSpinner"] as CRServo
        val spinner = Spinner(leftSpinner, rightSpinner)
        val bucket = this.hardwareMap["bucketServo"] as Servo
        var switch = this.hardwareMap["touch.sensor"] as TouchSensor
        var lastSwitch = switch.isPressed
        var gp1 = GamepadState(this.gamepad1)
        val gp2 = GamepadState(this.gamepad2)
        val rd = GamepadButton(gp2, Gamepad::dpad_right)
        this.waitForStart()
        while (this.opModeIsActive()) {
            gp1.cycle()
            gp2.cycle()

            val currSwitch = switch.isPressed

            // arm reaches bottom
            if (currSwitch && !lastSwitch) {
                arm.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
                arm.mode = DcMotor.RunMode.RUN_USING_ENCODER
                ratchet.activate()
                Thread.sleep(500)
                bucket.position = 0.225
            }

            // going up while arm is at bottom
            if (ratchet.engaged() && (-gp2.current.right_stick_y > 0 && currSwitch || -gp2.current.right_stick_y < 0 && !currSwitch /* should never happen */)) {
                ratchet.inactivate()
                Thread.sleep(500)
            }

            // arm going up
            if (-gp2.current.right_stick_y > 0 && arm.currentPosition < 7000) {
                arm.power = -gp2.current.right_stick_y * 0.5
                // going down
            } else if (-gp2.current.right_stick_y < 0 && !currSwitch) {
                bucket.position = 0.5
                arm.power = -gp2.current.right_stick_y * 0.5
            } else arm.power = 0.0

            // elevator and spinner
            elevator.power = gp2.current.left_stick_y.toDouble()
            if (gp2.current.left_stick_y.sign != 0f) spinner.on(gp2.current.left_stick_y > 0) else spinner.off()

            rd.ifIsToggled { bucket.position = 0.0 }

            lastSwitch = currSwitch

            this.telemetry.addData("arm pos", arm.currentPosition)

            /*
              gets the current Position (x & y in mm, and heading in degrees) of the robot, and prints it.
            */
            val pos = odometry.position
            val data = "X: %.3f, Y: %.3f, H: %.3f".format(
                pos.getX(DistanceUnit.MM),
                pos.getY(DistanceUnit.MM),
                pos.getHeading(AngleUnit.DEGREES)
            )
            this.telemetry.addData("Position", data)

            /*
            gets the current Velocity (x & y in mm/sec and heading in degrees/sec) and prints it.
             */
            val vel = odometry.velocity
            val velocity = "XVel: %.3f, YVel: %.3f, HVel: %.3f".format(
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

    class Spinner(val left: CRServo, val right: CRServo) {
        fun on(inwards: Boolean) = set(if (inwards) 1.0 else -1.0)
        fun off() = set(0.0)
        fun get() = left.power == 0.0
        fun set(num: Double) {
            left.power = num;
            right.power = num;
        }
    }

    // active ~ closed
    class ServoWrapper(val servo: Servo, val active: Double, val inactive: Double) {
        var toggled = false;

        fun toggle(): Boolean {
            toggled = !toggled
            servo.position = if (toggled) active else inactive
            return toggled
        }

        fun engaged() = toggled

        fun activate() {
            toggled = true
            servo.position = active
        }

        fun inactivate() {
            toggled = false;
            servo.position = inactive
        }
    }
}
