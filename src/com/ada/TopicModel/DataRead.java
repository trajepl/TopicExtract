package com.ada.TopicModel;

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ljp on 12/20/15.
 * Intro: POI Organization Group(POG) read.
 * List<String[]> readPOG(List<String[]> POGList)
 *
 */

class DataRead {
    private static String INFO_PATH = "result.txt";
    //private static String INFO_PATH = "simplePOG.txt";
    //private static String JS_INFO = "createGroup1.js";
    private static String charSetName = "utf-8";

    double[][] logLat= new double[702][2];
    //int[][] lable = new int[702][20];
    //private ArrayList<Integer> lable = new ArrayList<Integer>();

    double round(double value, int scale, int roundingMode) {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(scale, roundingMode);
        double d = bd.doubleValue();
        bd = null;
        return d;
    }

    //以info[i][4]为一个组读取数据
    public List<String[]> readPOG(List<String[]> POGList) throws IOException {
        File f = new File(INFO_PATH);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(f), charSetName);
        BufferedReader reader = new BufferedReader(isr);

        String line;
        ArrayList POGTmp = new ArrayList();
        boolean flag_read = false;
        boolean flag_firstLine = false;
        int flag1 = 0;
        int flag2 = 0;

        double sum = 0;
        int loglatCount = 0;
        int count = 0;

        while ((line = reader.readLine()) != null) {

            String[] data = line.split(" +");
            if(!flag_read) {
                if(!flag_firstLine) {
                    flag1 = Integer.parseInt(data[4]);
                    sum += Double.parseDouble(data[0]);
                    logLat[loglatCount][0] = Double.parseDouble(data[1]);
                    count++;
                    POGTmp.add(data[2]);
                    flag_read = true;
                }
                else {
                    flag1 = Integer.parseInt(data[4]);
                    if (flag2 == flag1) {
                        sum += Double.parseDouble(data[0]);
                        logLat[loglatCount][0] = Double.parseDouble(data[1]);
                        count++;
                        POGTmp.add(data[2]);
                        flag_read = true;
                    } else {
                        String[] POG = (String[]) POGTmp.toArray(new String[0]);
                        flag_read = false;
                        POGTmp.clear();
                        logLat[loglatCount++][1] = round(sum / count, 4,
                                BigDecimal.ROUND_HALF_DOWN);
                        sum =0;
                        count = 0;
                        POGTmp.add(data[2]);
                        POGList.add(POG);
                        flag2 = flag1;
                        continue;
                    }
                }
                flag_firstLine = true;

            } else {
                flag2 = Integer.parseInt(data[4]);

                if (flag1 != flag2) {
                    String[] POG = (String[]) POGTmp.toArray(new String[0]);
                    flag_read = false;
                    POGTmp.clear();
                    logLat[loglatCount++][1] = round(sum / count, 4,
                            BigDecimal.ROUND_HALF_DOWN);
                    sum = 0;
                    count = 0;
                    POGTmp.add(data[2]);
                    POGList.add(POG);
                } else {
                    sum += Double.parseDouble(data[0]);
                    logLat[loglatCount][0] = Double.parseDouble(data[1]);
                    count++;
                    POGTmp.add(data[2]);
                }
            }
        }
        logLat[loglatCount++][1] = sum / count;

        String[] POG = (String[]) POGTmp.toArray(new String[0]);
        POGTmp.clear();
        POGList.add(POG);
        reader.close();
        return POGList;
    }
}
