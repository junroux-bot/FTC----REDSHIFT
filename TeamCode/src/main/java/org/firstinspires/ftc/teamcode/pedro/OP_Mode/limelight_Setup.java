package org.firstinspires.ftc.teamcode.pedro.OP_Mode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;

public class limelight_Setup {
    IMU_Setup imu = new IMU_Setup();
    private Limelight3A camera;

    public void camera_init(HardwareMap hwmap,String camera_Name,int mode) {
        camera = hwmap.get(Limelight3A.class, camera_Name);
        camera.pipelineSwitch(mode);
        imu.intt(hwmap);

    }

    public void start() {
        camera.start();

    }

    public double GetCamera_data(String Data_type){
        double yawdegrees = imu.getHeading(AngleUnit.DEGREES);
        camera.updateRobotOrientation(yawdegrees);
        LLResult llResult =camera.getLatestResult();
        if(llResult != null && llResult.isValid()){
            Pose3D botpose= llResult.getBotpose_MT2();
            if ("tx".equalsIgnoreCase(Data_type)) {
                return llResult.getTx();
            } else if ("ty".equalsIgnoreCase(Data_type)) {
                return llResult.getTy();
            } else if ("ta".equalsIgnoreCase(Data_type)) {
                return llResult.getTa();
            }

        }
        return 0.0;

    }


    public double get_DistanceFrom(double ta) {
        double scale = 46927.56;

        if (ta > 0) {
            return Math.sqrt(scale / ta);
        }

        return 0.0;
    }


}
