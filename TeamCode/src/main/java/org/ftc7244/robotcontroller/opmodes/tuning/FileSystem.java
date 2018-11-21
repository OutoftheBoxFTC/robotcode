package org.ftc7244.robotcontroller.opmodes.tuning;

import android.content.Context;
import android.os.Environment;

import com.qualcomm.robotcore.util.RobotLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileSystem {

    public static boolean saveToFile(String data, String path, Context context){
        File file = new File(context.getFilesDir(), path);
        try {
            FileWriter writer = new FileWriter(file);
            writer.append(data);
            writer.flush();
            writer.close();
        }
        catch (IOException e){
            return false;
        }
        return true;
    }

    public static String loadFromFile(String path, Context context){
        File file = new File(context.getFilesDir(), path);
        try {
            Scanner fileReader = new Scanner(file);
            StringBuilder output = new StringBuilder();
            while (fileReader.hasNextLine()){
                output.append(fileReader.nextLine());
            }
            return output.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
