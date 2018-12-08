package org.ftc7244.robotcontroller.opmodes.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.I2cDevice;
import com.qualcomm.robotcore.hardware.I2cDeviceSynch;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

@TeleOp
/*
Bytes    16-bit word    Description
        ----------------------------------------------------------------
        0, 1     y              sync: 0xaa55=normal object, 0xaa56=color code object
        2, 3     y              checksum (sum of all 16-bit words 2-6, that is, bytes 4-13)
        4, 5     y              signature number
        6, 7     y              x center of object
        8, 9     y              y center of object
        10, 11   y              width of object
        12, 13   y              height of object
        */
public class PixyCamTests extends OpMode {
    I2cDeviceSynch pixy;
    I2cDevice pixyI2c;
    byte[][] bytes = new byte[12][];
    byte[] data = new byte[12];
    List<Short> shorts;
    boolean showData = false;
    @Override
    public void init() {
        pixy = hardwareMap.i2cDeviceSynch.get("pixy");
        pixy.setI2cAddress(I2cAddr.create7bit(0x54));
        I2cDeviceSynch.ReadWindow readWindow = new I2cDeviceSynch.ReadWindow (1, 26, I2cDeviceSynch.ReadMode.REPEAT);
        pixy.setReadWindow(readWindow);
        pixy.engage();

    }

    @Override
    public void loop() {
/*        telemetry.addData("Byte 0", pixy.read8(0));
        telemetry.addData("Byte 1", pixy.read8(1));
        telemetry.addData("Byte 2", pixy.read8(2));
        telemetry.addData("Byte 3", pixy.read8(3));
        telemetry.addData("Byte 4", pixy.read8(4));
        telemetry.addData("Byte 5", pixy.read8(5));
        telemetry.addData("Byte 6", pixy.read8(6));
        telemetry.addData("Byte 7", pixy.read8(7));
        telemetry.addData("Byte 8", pixy.read8(8));
        telemetry.addData("Byte 9", pixy.read8(9));
        telemetry.addData("Byte 10", pixy.read8(10));
        telemetry.addData("Byte 11", pixy.read8(11));
        telemetry.addData("Byte 12", pixy.read8(12));
        telemetry.addData("Byte 13", pixy.read8(13));
*/      data = pixy.read(0, 26);
        //for(int i = 0; i < data.length; i ++){
        //    data[i] = pixy.read8(i);
        //}
        for(int i = 0; i < bytes.length; i ++) {
            bytes[i] = pixy.read(i, 13);
        }
        //for(int i = 0; i < bytes.length; i ++){
        //    telemetry.addLine(bytes[i][0] + " " + bytes[i][1] + " " + bytes[i][2] + " " + bytes[i][3] + " " + bytes[i][4] + " " + bytes[i][5] + " " + bytes[i][6] + " " + bytes[i][7] + " " + bytes[i][8] + " " + bytes[i][9] + " " + bytes[i][10] + " " + bytes[i][11] + " " + bytes[i][12]);
        //}
        shorts = endianToShort(data);
        for(int i = 0; i < shorts.size(); i ++){
            telemetry.addData("Short " + i, shorts.get(i));
        }
        if()
        telemetry.update();
    }

    private short bytesToShort(byte a, byte b) {
        short sh = (short)a;
        sh <<= 8;
        short ret = (short)(sh | b);
        return ret;
    }

    private List<Short> endianToShort(byte[] byteArray){
        List<Short> shorts = new ArrayList<Short>();
        ByteBuffer bb = ByteBuffer.wrap(byteArray);
        bb.order( ByteOrder.LITTLE_ENDIAN);
        while( bb.hasRemaining()) {
            shorts.add(bb.getShort());
        }
        return shorts;
    }
}