package org.firstinspires.ftc.teamcode.drivetrain

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.*
import java.lang.Exception

@TeleOp(name = "DriveTrain Test")
// @Disabled
class DriveTrain : LinearOpMode() {
    private lateinit var leftBackMotor: DcMotor
    private lateinit var rightBackMotor: DcMotor
    private lateinit var rightFrontMotor: DcMotor
    private lateinit var leftFrontMotor: DcMotor


    private fun initMotors(): Boolean {
        try {
            leftBackMotor = hardwareMap.dcMotor["leftBack"]
            rightBackMotor = hardwareMap.dcMotor["rightBack"]
            rightFrontMotor = hardwareMap.dcMotor["rightFront"]
            leftFrontMotor = hardwareMap.dcMotor["leftFront"]

            leftFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            leftBackMotor.direction = DcMotorSimple.Direction.REVERSE
            rightBackMotor.direction = DcMotorSimple.Direction.REVERSE

            return true

        } catch (e: Exception) {
           telemetry.addData("Exception", e)
           return false
        }
    }

    override fun runOpMode() {

        waitForStart()

        if (!initMotors()) {
            telemetry.addData("Motor Initialization", "Failed")
            telemetry.update()
            terminateOpModeNow()
        } else {
            telemetry.addData("Motor Initialization", "Success")
            telemetry.update()
        }

        while (opModeIsActive()){
            leftBackMotor.power = 0.1
        }
    }
}