package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseLinearOpMode : LinearOpMode() {
    protected lateinit var leftBackMotor: DcMotor
    protected lateinit var rightBackMotor: DcMotor
    protected lateinit var rightFrontMotor: DcMotor
    protected lateinit var leftFrontMotor: DcMotor
    protected lateinit var odometry: GoBildaPinpointDriver
    protected lateinit var allMotors: Array<DcMotor>


    protected fun initDriveTrain() {
        try {
            this.leftBackMotor = this.hardwareMap.dcMotor["leftRear"]
            this.rightBackMotor = this.hardwareMap.dcMotor["rightRear"]
            this.rightFrontMotor = this.hardwareMap.dcMotor["rightFront"]
            this.leftFrontMotor = this.hardwareMap.dcMotor["leftFront"]

            this.leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            this.rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            this.leftBackMotor.direction = DcMotorSimple.Direction.REVERSE
            this.rightBackMotor.direction = DcMotorSimple.Direction.FORWARD

            this.allMotors = arrayOf(leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor)

            this.odometry = this.hardwareMap.get(GoBildaPinpointDriver::class.java, "odometry")
            this.odometry.setOffsets(95.0, 0.0)
            this.odometry.setEncoderResolution(37.25135125)
            this.odometry.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED
            )
            odometry.resetPosAndIMU()

            telemetry.addData("Drive Train Initialization", "Success")

        } catch (e: Exception) {
            telemetry.addData("Exception", e)
        }

        telemetry.update()
    }
}