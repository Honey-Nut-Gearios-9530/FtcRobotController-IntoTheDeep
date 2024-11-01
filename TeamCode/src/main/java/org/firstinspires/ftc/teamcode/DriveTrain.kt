package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.modular.*

@TeleOp(name = "Lucas DriveTrain")
// @Disabled
class DriveTrain : LinearOpMode() {
    private lateinit var leftBackMotor: DcMotor
    private lateinit var rightBackMotor: DcMotor
    private lateinit var rightFrontMotor: DcMotor
    private lateinit var leftFrontMotor: DcMotor
    private lateinit var allMotors: Array<DcMotor>
    private var powerMultiplier = ResettableState(1.0)
    private var highSpeed = true
    private lateinit var gp1: GamepadState

    override fun runOpMode() {

        if (!initialize()) {
            telemetry.addData("Device Initialization", "Failed")
            telemetry.update()
            terminateOpModeNow()
        } else {
            telemetry.addData("Device Initialization", "Success")
            telemetry.update()
        }

        waitForStart()

        while (opModeIsActive()) {
            gp1.cycle()

            val motorPower = arrayOf(
                gp1.current.left_stick_x - gp1.current.left_stick_y + gp1.current.right_stick_x,
                -gp1.current.left_stick_x - gp1.current.left_stick_y - gp1.current.right_stick_x,
                -gp1.current.left_stick_x - gp1.current.left_stick_y + gp1.current.right_stick_x,
                gp1.current.left_stick_x - gp1.current.left_stick_y - gp1.current.right_stick_x,
            )

            // Toggle speeds: untested
            if (gp1.current.a && !gp1.past.a){
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

    private fun initialize(): Boolean {
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

            gp1 = GamepadState(gamepad1)

            return true

        } catch (e: Exception) {
            telemetry.addData("Exception", e)
            return false
        }
    }
}