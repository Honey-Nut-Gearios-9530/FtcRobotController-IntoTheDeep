package org.firstinspires.ftc.teamcode.drivetrain

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.*

@TeleOp(name = "DriveTrain Test")
// @Disabled
class DriveTrain : LinearOpMode() {
    private lateinit var leftBackMotor: DcMotor
    private lateinit var rightBackMotor: DcMotor
    private lateinit var rightFrontMotor: DcMotor
    private lateinit var leftFrontMotor: DcMotor
    private lateinit var allMotors: Array<DcMotor>

    private val powerMultiplier = 0.75

    override fun runOpMode() {

        if (!initMotors()) {
            telemetry.addData("Motor Initialization", "Failed")
            telemetry.update()
            terminateOpModeNow()
        } else {
            telemetry.addData("Motor Initialization", "Success")
            telemetry.update()
        }

        waitForStart()

        while (opModeIsActive()) {
            val motorPower = arrayOf(
                -gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x,
                gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x,
                gamepad1.left_stick_x - gamepad1.left_stick_y + gamepad1.right_stick_x,
                -gamepad1.left_stick_x - gamepad1.left_stick_y - gamepad1.right_stick_x,
            )

            allMotors.forEachIndexed { i, m ->
                m.power =
                    (motorPower[i] * powerMultiplier).coerceIn(-powerMultiplier, powerMultiplier)
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

        } catch (e: NullPointerException) {
            telemetry.addData("Exception", e)
            return false
        }
    }
}