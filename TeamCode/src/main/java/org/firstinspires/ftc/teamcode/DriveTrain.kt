package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.modular.GamepadButton
import org.firstinspires.ftc.teamcode.modular.MutableState

@TeleOp(name = "Lucas DriveTrain")
// @Disabled
class DriveTrain : LinearOpMode() {
    private lateinit var leftBackMotor: DcMotor
    private lateinit var rightBackMotor: DcMotor
    private lateinit var rightFrontMotor: DcMotor
    private lateinit var leftFrontMotor: DcMotor
    private lateinit var allMotors: Array<DcMotor>
    private lateinit var buttonA: GamepadButton

    private var powerMultiplier = MutableState(0.75)
    private var highSpeed = true

    override fun runOpMode() {

        if (!initMotors()) {
            telemetry.addData("Motor Initialization", "Failed")
            telemetry.update()
            terminateOpModeNow()
        } else {
            telemetry.addData("Motor Initialization", "Success")
            telemetry.update()
        }

        buttonA = GamepadButton(gamepad1::a)

        waitForStart()

        while (opModeIsActive()) {
            val motorPower = arrayOf(
                gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x,
                -gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x,
                -gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x,
                gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x,
            )

            // Toggle speeds: untested
            if (buttonA.canPress && buttonA.value){
                when (highSpeed){
                    true -> powerMultiplier.value *= 0.25
                    false -> powerMultiplier.reset()
                }
                highSpeed = !highSpeed
            }

            allMotors.forEachIndexed { i, m ->
                m.power =
                    (motorPower[i] * powerMultiplier.value)
                        .coerceIn(-powerMultiplier.value, powerMultiplier.value)
            }
        }
    }

    private fun initMotors(): Boolean {
        try {
            leftBackMotor = hardwareMap.dcMotor["leftBack"]
            rightBackMotor = hardwareMap.dcMotor["rightBack"]
            rightFrontMotor = hardwareMap.dcMotor["rightFront"]
            leftFrontMotor = hardwareMap.dcMotor["leftFront"]

            leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            leftBackMotor.direction = DcMotorSimple.Direction.REVERSE
            rightBackMotor.direction = DcMotorSimple.Direction.FORWARD

            allMotors = arrayOf(leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor)

            return true

        } catch (e: Exception) {
            telemetry.addData("Exception", e)
            return false
        }
    }
}