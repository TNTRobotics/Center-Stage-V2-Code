package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="driveTrain", group="Driving")

public class driveTrain extends LinearOpMode {

    DcMotor lfD = null;
    DcMotor lbD = null;
    DcMotor rfD = null;
    DcMotor rbD = null;

    DcMotor pivot = null;
    ElapsedTime rTime = null;
    double speedMultiplier = 1;

    public enum TARGET {
        POSITION0, POSITION1
    }



    @Override
    public void runOpMode() throws InterruptedException {
        rTime = new ElapsedTime();
        lfD = hardwareMap.dcMotor.get("lfD");
        lbD = hardwareMap.dcMotor.get("lbD");
        rfD = hardwareMap.dcMotor.get("rfD");
        rbD = hardwareMap.dcMotor.get("rbD");

        pivot = hardwareMap.dcMotor.get("pivot");
        rfD.setDirection(DcMotor.Direction.REVERSE);
        rbD.setDirection(DcMotor.Direction.REVERSE);
        // BRAKE
        lfD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        lbD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rfD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rbD.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        pivot.setDirection(DcMotorSimple.Direction.FORWARD);
        pivot.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        pivot.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        pivot.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Servo clawServo;
        Servo clawServo1;
        Servo rotateServo;

        clawServo = hardwareMap.get(Servo.class, "clawServo");
        clawServo.setPosition(.8);
        clawServo1 = hardwareMap.get(Servo.class, "clawServo1");
        clawServo1.setPosition(0.2);
        rotateServo = hardwareMap.get(Servo.class, "rotateServo");
        rotateServo.setPosition(0);


        int position0Target = 0;
        int position1Target = 720;
        int topPosition = 600;
        double kP = 0.0018;

        TARGET targetPivotPosition = TARGET.POSITION0;
        waitForStart();
        while (opModeIsActive()) {

            if (gamepad1.left_stick_y != 0 || gamepad1.left_stick_x != 0 || gamepad1.right_stick_x != 0) {
                lfD.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x - gamepad1.right_stick_x) * speedMultiplier);
                lbD.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_x) * speedMultiplier);
                rfD.setPower((gamepad1.left_stick_y + gamepad1.left_stick_x + gamepad1.right_stick_x) * speedMultiplier);
                rbD.setPower((gamepad1.left_stick_y - gamepad1.left_stick_x + gamepad1.right_stick_x) * speedMultiplier);
            } else {
                lfD.setPower(0);
                lbD.setPower(0);
                rfD.setPower(0);
                rbD.setPower(0);
            }

            if (gamepad1.left_trigger != 0) {
                //pivot.setPower(-gamepad1.left_trigger / 3);
                 clawServo.setPosition(0);
            }
            if(gamepad1.right_trigger != 0) {
                //pivot.setPower(gamepad1.right_trigger / 3);
                clawServo1.setPosition(1);
            }

            if (gamepad1.left_bumper) {
                clawServo.setPosition(.2);
            }

            if(gamepad1.right_bumper){
                clawServo1.setPosition(0.8);
            }
            /*
            if (pivotPosition < -10) {
                pivotPosition = -10;
            }
            if (pivotPosition > 800) {
                pivotPosition = 800;
            }

             */
            if (gamepad1.dpad_down) {
                rotateServo.setPosition(.6);
            } if (gamepad1.dpad_up){
                rotateServo.setPosition(0.1);
            }
            if (gamepad1.triangle) {
                targetPivotPosition = TARGET.POSITION0;
            } else if (gamepad1.square) {
                targetPivotPosition = TARGET.POSITION1;
            }



            /**
             Here, it starts the actual set positions, it initializes everything for that including PID and the motors.
             **/

            int currentTargetPosition = 0;

            switch (targetPivotPosition) {
                case POSITION0:
                    currentTargetPosition = position0Target;
                    break;
                case POSITION1:
                    currentTargetPosition = position1Target;
                    break;
            }

            pivot.setPower((currentTargetPosition - pivot.getCurrentPosition()) * kP);

            double angle = pivot.getCurrentPosition();
            telemetry.addData("power: ", pivot.getPower());
            telemetry.addData("position: ", pivot.getCurrentPosition());
            telemetry.update();

        }
    }
}
