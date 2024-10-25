package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.*
import com.qualcomm.robotcore.util.ElapsedTime

@TeleOp(name = "Hello World OpMode", group = "Linear OpMode")
//@Disabled
class HelloWorld : LinearOpMode() {
    private val elapsedTime = ElapsedTime()

    override fun runOpMode() {
        waitForStart()
        while (opModeIsActive()) {

            telemetry.addData("Elapsed Time", elapsedTime.toString())

            if (!telemetry.update()) {
                throw Exception("Failed to update telemetry")
            }

            sleep(10000)
        }
    }
}