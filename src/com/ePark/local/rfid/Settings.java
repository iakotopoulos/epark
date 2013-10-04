package com.ePark.local.rfid;

import java.io.*;
import CSLibrary.Constants.*;
import CSLibrary.Structures.*;

public class Settings {
    public static int Channel_number = 0;
    public static boolean Lbt = false;
    public static int Link_profile = 2;
    public static int Power = 300;
    public static int Region = RegionCode.UNKNOWN;
    public static boolean FixedChannel = false;

    public static boolean custInventory_continuous = true;
    public static boolean custInventory_blocking_mode = false;
    public static TagGroup tagGroup = new TagGroup(Selected.ALL, Session.S0, SessionTarget.A);
    public static int singulation = SingulationAlgorithm.DYNAMICQ;

    public static FixedQParms fixedQ = new FixedQParms(7,0,1,0);
    public static DynamicQParms dynamicQ = new DynamicQParms(7,0,15,0,0,1);
    public static DynamicQAdjustParms dynamicQAdj = new DynamicQAdjustParms(7,0,15,0,0,1);
    public static DynamicQThresholdParms dynamicQThres = new DynamicQThresholdParms(7,0,15,0,0,1);

    public static SingulationAlgorithmParms GetSingulationAlg()
    {
        switch(singulation)
        {
            case SingulationAlgorithm.DYNAMICQ:
                return dynamicQ;
            case SingulationAlgorithm.DYNAMICQ_ADJUST:
                return dynamicQAdj;
            case SingulationAlgorithm.DYNAMICQ_THRESH:
                return dynamicQThres;
            case SingulationAlgorithm.FIXEDQ:
                return fixedQ;
            default:
                return new SingulationAlgorithmParms();
        }
    }

    public static boolean Save()
    {
        try
        {
            FileWriter fstream = new FileWriter("myset.txt");
            BufferedWriter out = new BufferedWriter(fstream);

            // save general options
            out.write(Integer.toString(Channel_number));
            out.newLine();
            out.write(Integer.toString(booleantoInt(Lbt)));
            out.newLine();
            out.write(Integer.toString(Link_profile));
            out.newLine();
            out.write(Integer.toString(Power));
            out.newLine();
            out.write(Integer.toString(Region));
            out.newLine();
            out.write(Integer.toString(booleantoInt(FixedChannel)));

            // save inventory
            out.newLine();
            out.write(Integer.toString(booleantoInt(custInventory_continuous)));
            out.newLine();
            out.write(Integer.toString(booleantoInt(custInventory_blocking_mode)));
            out.newLine();
            out.write(Integer.toString(tagGroup.selected));
            out.newLine();
            out.write(Integer.toString(tagGroup.session));
            out.newLine();
            out.write(Integer.toString(tagGroup.target));
            out.newLine();
            out.write(Integer.toString(singulation));

            // save fixed Q
            out.newLine();
            out.write(Integer.toString(fixedQ.qValue));
            out.newLine();
            out.write(Integer.toString(fixedQ.retryCount));
            out.newLine();
            out.write(Integer.toString(fixedQ.repeatUntilNoTags));
            out.newLine();
            out.write(Integer.toString(fixedQ.toggleTarget));

            // save dynamic Q
            out.newLine();
            out.write(Integer.toString(dynamicQ.startQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQ.minQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQ.maxQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQ.maxQueryRepCount));
            out.newLine();
            out.write(Integer.toString(dynamicQ.retryCount));
            out.newLine();
            out.write(Integer.toString(dynamicQ.toggleTarget));

            // save dynamic Q Adj
            out.newLine();
            out.write(Integer.toString(dynamicQAdj.startQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQAdj.minQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQAdj.maxQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQAdj.maxQueryRepCount));
            out.newLine();
            out.write(Integer.toString(dynamicQAdj.retryCount));
            out.newLine();
            out.write(Integer.toString(dynamicQAdj.toggleTarget));

            // save dynamic Q Thres
            out.newLine();
            out.write(Integer.toString(dynamicQThres.startQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQThres.minQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQThres.maxQValue));
            out.newLine();
            out.write(Integer.toString(dynamicQThres.thresholdMultiplier));
            out.newLine();
            out.write(Integer.toString(dynamicQThres.retryCount));
            out.newLine();
            out.write(Integer.toString(dynamicQThres.toggleTarget));

            out.close();
        }
        catch(IOException ex)
        {
            return false;
        }
        return true;
    }

    public static void Load()
    {
        try
        {
            FileInputStream  fstream = new FileInputStream ("myset.txt");
            DataInputStream  in = new DataInputStream (fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            // load general options
            Channel_number = Integer.parseInt(br.readLine());
            Lbt = inttoBoolean(Integer.parseInt(br.readLine()));
            Link_profile = Integer.parseInt(br.readLine());
            Power = Integer.parseInt(br.readLine());
            Region = Integer.parseInt(br.readLine());
            FixedChannel = inttoBoolean(Integer.parseInt(br.readLine()));

            // load inventory
            custInventory_continuous = inttoBoolean(Integer.parseInt(br.readLine()));
            custInventory_blocking_mode = inttoBoolean(Integer.parseInt(br.readLine()));
            tagGroup.selected = Integer.parseInt(br.readLine());
            tagGroup.session = Integer.parseInt(br.readLine());
            tagGroup.target = Integer.parseInt(br.readLine());
            singulation = Integer.parseInt(br.readLine());

            // load fixed Q
            fixedQ.qValue = Integer.parseInt(br.readLine());
            fixedQ.retryCount = Integer.parseInt(br.readLine());
            fixedQ.repeatUntilNoTags = Integer.parseInt(br.readLine());
            fixedQ.toggleTarget = Integer.parseInt(br.readLine());

            // load dynamic Q
            dynamicQ.startQValue = Integer.parseInt(br.readLine());
            dynamicQ.minQValue = Integer.parseInt(br.readLine());
            dynamicQ.maxQValue = Integer.parseInt(br.readLine());
            dynamicQ.maxQueryRepCount = Integer.parseInt(br.readLine());
            dynamicQ.retryCount = Integer.parseInt(br.readLine());
            dynamicQ.toggleTarget = Integer.parseInt(br.readLine());

            // load dynamic Q Adj
            dynamicQAdj.startQValue = Integer.parseInt(br.readLine());
            dynamicQAdj.minQValue = Integer.parseInt(br.readLine());
            dynamicQAdj.maxQValue = Integer.parseInt(br.readLine());
            dynamicQAdj.maxQueryRepCount = Integer.parseInt(br.readLine());
            dynamicQAdj.retryCount = Integer.parseInt(br.readLine());
            dynamicQAdj.toggleTarget = Integer.parseInt(br.readLine());

            // load dynamic Q Thres
            dynamicQThres.startQValue = Integer.parseInt(br.readLine());
            dynamicQThres.minQValue = Integer.parseInt(br.readLine());
            dynamicQThres.maxQValue = Integer.parseInt(br.readLine());
            dynamicQThres.thresholdMultiplier = Integer.parseInt(br.readLine());
            dynamicQThres.retryCount = Integer.parseInt(br.readLine());
            dynamicQThres.toggleTarget = Integer.parseInt(br.readLine());

            in.close();
        }
        catch(IOException ex)
        {
        }
    }

    private static int booleantoInt(boolean in)
    {
        return in ? 1 : 0;
    }

    private static boolean inttoBoolean(int in)
    {
        if (in == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}

