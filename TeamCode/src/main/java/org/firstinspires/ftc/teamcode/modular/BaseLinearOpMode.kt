package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.DcMotorSimple
import org.firstinspires.ftc.teamcode.GoBildaPinpointDriver

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseLinearOpMode : LinearOpMode() {
    protected lateinit var leftBackMotor: DcMotorEx
    protected lateinit var rightBackMotor: DcMotorEx
    protected lateinit var rightFrontMotor: DcMotorEx
    protected lateinit var leftFrontMotor: DcMotorEx
    protected lateinit var odometry: GoBildaPinpointDriver
    protected lateinit var allMotors: Array<DcMotorEx>

    protected fun initDriveTrain() {
        // TODO: find a way to print this exception while blocking starting the program
        try {
            this.leftBackMotor = this.hardwareMap["leftRear"] as DcMotorEx
            this.rightBackMotor = this.hardwareMap["rightRear"] as DcMotorEx
            this.rightFrontMotor = this.hardwareMap["rightFront"] as DcMotorEx
            this.leftFrontMotor = this.hardwareMap["leftFront"] as DcMotorEx

            this.leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            this.rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            this.leftBackMotor.direction = DcMotorSimple.Direction.REVERSE
            this.rightBackMotor.direction = DcMotorSimple.Direction.FORWARD

            this.allMotors = arrayOf(leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor)

            this.odometry = this.hardwareMap["odometry"] as GoBildaPinpointDriver
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