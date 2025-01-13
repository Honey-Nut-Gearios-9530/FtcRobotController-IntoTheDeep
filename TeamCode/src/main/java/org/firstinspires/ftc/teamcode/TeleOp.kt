package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
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
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.GamepadState
import org.firstinspires.ftc.teamcode.modular.ToggleableState
import java.util.Locale
import kotlin.math.abs
import kotlin.math.sign

@TeleOp(name = "DriveTrain")
@Suppress("unused")
// @Disabled
class TeleOp : LinearOpMode() {
    protected lateinit var leftBack: DcMotorEx
    protected lateinit var rightBack: DcMotorEx
    protected lateinit var rightFront: DcMotorEx
    protected lateinit var leftFront: DcMotorEx
    protected lateinit var allMotors: Array<DcMotorEx>
    protected lateinit var odometry: GoBildaPinpointDriver
    protected lateinit var spinner: Spinner
    protected lateinit var bucket: Servo
    protected lateinit var switch: TouchSensor
    protected lateinit var elevator: DcMotorEx
    protected lateinit var arm: DcMotorEx
    protected lateinit var ratchet: ServoWrapper


    // kotlin does not do numeric type promotion, if the 3rd arg is just "1" than T cannot be inferred
    private var power = ToggleableState(2, 0.33, 0.67, 1.0)
    private lateinit var gp1: GamepadState
    private lateinit var gp2: GamepadState


    protected fun initDriveTrain() {
        this.leftBack = this.hardwareMap["left_back"] as DcMotorEx
        this.rightBack = this.hardwareMap["right_back"] as DcMotorEx
        this.rightFront = this.hardwareMap["right_front"] as DcMotorEx
        this.leftFront = this.hardwareMap["left_front"] as DcMotorEx

        this.leftFront.direction = DcMotorSimple.Direction.REVERSE
        this.rightFront.direction = DcMotorSimple.Direction.FORWARD
        this.leftBack.direction = DcMotorSimple.Direction.REVERSE
        this.rightBack.direction = DcMotorSimple.Direction.FORWARD

        this.allMotors = arrayOf(leftFront, rightFront, leftBack, rightBack)

        this.odometry = this.hardwareMap["odometry"] as GoBildaPinpointDriver
        this.odometry.setOffsets(95.0, 0.0)
        this.odometry.setEncoderResolution(37.25135125)
        this.odometry.setEncoderDirections(
            GoBildaPinpointDriver.EncoderDirection.FORWARD,
            GoBildaPinpointDriver.EncoderDirection.REVERSED
        )
        odometry.resetPosAndIMU()
    }

    fun initElse() {
        arm = this.hardwareMap["arm"] as DcMotorEx
        arm.direction = DcMotorSimple.Direction.REVERSE
        arm.mode = DcMotor.RunMode.RUN_USING_ENCODER
        elevator = this.hardwareMap["elevator"] as DcMotorEx
        elevator.direction = DcMotorSimple.Direction.REVERSE
        elevator.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        ratchet = ServoWrapper(this.hardwareMap["ratchet"] as Servo, 0.1, 0.0)
        ratchet.inactivate()
        val leftSpinner = this.hardwareMap["left_spinner"] as CRServo
        leftSpinner.direction = DcMotorSimple.Direction.REVERSE
        val rightSpinner = this.hardwareMap["right_spinner"] as CRServo
        spinner = Spinner(leftSpinner, rightSpinner)
        bucket = this.hardwareMap["bucket"] as Servo
        switch = this.hardwareMap["touch_sensor"] as TouchSensor
    }

    override fun runOpMode() {
        telemetry.msTransmissionInterval = 100

        gp1 = GamepadState(gamepad1)
        gp2 = GamepadState(this.gamepad2)

        val toggleButtonMap = mapOf(
            GamepadButton(gp1, Gamepad::left_bumper) to power::left,
            GamepadButton(gp1, Gamepad::right_bumper) to power::right,
            GamepadButton(gp2, Gamepad::dpad_right) to { bucket.position = 0.0 }
        )

        // TODO: try-catch this to print any errors / force stop the program?
        this.initDriveTrain()
        this.initElse()

        telemetry.addData("Drive Train Initialization", "Success")
        telemetry.update()

        this.waitForStart()

        var lastSwitch = switch.isPressed

        while (this.opModeIsActive()) {
            this.gp1.cycle()
            this.gp2.cycle()
            this.odometry.update()
            val pos = this.odometry.position;
            val data = String.format(
                Locale.US,
                "x: %.3f, y: %.3f, h: %.3f",
                pos.getX(DistanceUnit.MM),
                pos.getY(DistanceUnit.MM),
                pos.getHeading(AngleUnit.DEGREES)
            );
            telemetry.addData("pos", data);

            toggleButtonMap.forEach { it.key.ifIsToggled(it.value) }

            /* Calculates motor power in accordance with the allMotors array
               and formulas found here: https://github.com/brandon-gong/ftc-mecanum
             */
            val turnPower = -this.gp1.current.right_trigger + this.gp1.current.left_trigger

            val motorPower = arrayOf(
                this.gp1.current.left_stick_y - this.gp1.current.left_stick_x - turnPower,
                this.gp1.current.left_stick_y - this.gp1.current.left_stick_x + turnPower,
                this.gp1.current.left_stick_y + this.gp1.current.left_stick_x - turnPower,
                this.gp1.current.left_stick_y + this.gp1.current.left_stick_x + turnPower,
            )

            // Magnitude of the maximum value, not velocity
            val max = abs(motorPower.maxBy { abs(it) })

            // Normalize if greater the max is greater than 1
            if (max > 1) motorPower.forEachIndexed { i, _ -> motorPower[i] /= max }

            // Update the motors with the proper power
            this.allMotors.forEachIndexed { i, m ->
                m.power = motorPower[i].toDouble() * power.value
            }


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

            lastSwitch = currSwitch

            this.telemetry.addData("arm pos", arm.currentPosition)


            telemetry.update()
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
