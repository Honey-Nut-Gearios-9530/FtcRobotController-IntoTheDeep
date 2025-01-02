package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple

@Suppress("MemberVisibilityCanBePrivate")
abstract class BaseLinearOpMode : LinearOpMode() {
    protected lateinit var leftBackMotor: DcMotor
    protected lateinit var rightBackMotor: DcMotor
    protected lateinit var rightFrontMotor: DcMotor
    protected lateinit var leftFrontMotor: DcMotor
    protected lateinit var allMotors: Array<DcMotor>

    protected fun initDriveTrain() {
        try {
            try{
                this.leftBackMotor = this.hardwareMap.dcMotor["leftBack"]
                this.rightBackMotor = this.hardwareMap.dcMotor["rightBack"]
                this.rightFrontMotor = this.hardwareMap.dcMotor["rightFront"]
                this.leftFrontMotor = this.hardwareMap.dcMotor["leftFront"]
                telemetry.addLine("using dated config")
            }catch (e: Exception) {
                this.leftBackMotor = this.hardwareMap.dcMotor["leftRear"]
                this.rightBackMotor = this.hardwareMap.dcMotor["rightRear"]
                this.rightFrontMotor = this.hardwareMap.dcMotor["rightFront"]
                this.leftFrontMotor = this.hardwareMap.dcMotor["leftFront"]
                telemetry.addLine("using new config")
            }catch (e: Exception){
                telemetry.addLine("I am the creeper, catch me if you can ")
                telemetry.addData("ERROR: check motor config and change names in BaseLinearOpMode.kt", "Error")
            }

            this.leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
            this.rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
            this.leftBackMotor.direction = DcMotorSimple.Direction.REVERSE
            this.rightBackMotor.direction = DcMotorSimple.Direction.FORWARD

            this.allMotors = arrayOf(leftFrontMotor, rightFrontMotor, leftBackMotor, rightBackMotor)

            telemetry.addData("Drive Train Initialization", "Success")

        } catch (e: Exception) {
            telemetry.addData("Exception", e)
        }

        telemetry.update()
    }
}