package org.firstinspires.ftc.teamcode.modular

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import kotlin.math.abs
import kotlin.math.pow

@Suppress("unused")
@Autonomous(name = "Autonomous Park Only", group = "Auto", preselectTeleOp = "DriveTrain")
class AutoPark : BaseLinearOpMode() {
    override fun runOpMode() {
        this.initHardware(false)
        allMotors.forEach { it.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER }
        this.waitForStart()
        this.allMotors.forEach { it.power = 0.3 }
        this.odometry.update()
        val start = this.odometry.position
        val target = 215
        var done = false
        while (this.opModeIsActive() && !done) {
            this.odometry.update()
            val pos = this.odometry.position
            val curr = abs((pos.getX(DistanceUnit.MM) - start.getX(DistanceUnit.MM)).pow(2)) +
                    abs((pos.getY(DistanceUnit.MM) - start.getY(DistanceUnit.MM)).pow(2))
            telemetry.addData("current", curr)
            if (curr >= target * target) {
                done = true
            }
        }
        this.allMotors.forEach { it.power = 0.0 }
    }
}
