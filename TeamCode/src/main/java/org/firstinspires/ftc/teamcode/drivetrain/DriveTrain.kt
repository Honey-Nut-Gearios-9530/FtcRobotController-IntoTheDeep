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

    private val power = 0.5

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

        while (opModeIsActive()){
            if (gamepad1.dpad_up)
                leftFrontMotor.power = 0.5
            else
                leftFrontMotor.power = 0.0
        }
    }

    private fun initMotors(): Boolean {
        try {
            leftBackMotor = hardwareMap.dcMotor["leftBack"]
            rightBackMotor = hardwareMap.dcMotor["rightBack"]
            rightFrontMotor = hardwareMap.dcMotor["rightFront"]
            leftFrontMotor = hardwareMap.dcMotor["leftFront"]

            leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            rightFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            leftBackMotor.direction = DcMotorSimple.Direction.FORWARD
            rightBackMotor.direction = DcMotorSimple.Direction.FORWARD

            return true

        } catch (e: NullPointerException) {
            telemetry.addData("Exception", e)
            return false
        }
    }
}