package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.hardware.*
import org.firstinspires.ftc.teamcode.modular.ResettableState

@TeleOp(name = "Lucas DriveTrain")
// @Disabled
class DriveTrain : LinearOpMode() {
    private lateinit var leftBackMotor: DcMotor
    private lateinit var rightBackMotor: DcMotor
    private lateinit var rightFrontMotor: DcMotor
    private lateinit var leftFrontMotor: DcMotor
    private lateinit var allMotors: Array<DcMotor>
    private var powerMultiplier = ResettableState(0.75)
    private var highSpeed = true
    private val pastGamepadState = Gamepad()
    private val currentGamepadState = Gamepad()

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
            pastGamepadState.copy(currentGamepadState)
            currentGamepadState.copy(gamepad1)

            val motorPower = arrayOf(
                currentGamepadState.left_stick_x - currentGamepadState.left_stick_y + currentGamepadState.right_stick_x,
                -currentGamepadState.left_stick_x - currentGamepadState.left_stick_y - currentGamepadState.right_stick_x,
                -currentGamepadState.left_stick_x - currentGamepadState.left_stick_y + currentGamepadState.right_stick_x,
                currentGamepadState.left_stick_x - currentGamepadState.left_stick_y - currentGamepadState.right_stick_x,
            )

            // Toggle speeds: untested
            if (currentGamepadState.a && !pastGamepadState.a){
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