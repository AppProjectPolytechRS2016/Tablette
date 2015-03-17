package com.app.rs_2016;

/**
 * Created by Sarion on 01/03/2015.
 */


public class CheckUserChoice {

    /**
     *
     * @param toTest0 : first IP byte
     * @param toTest1 : second IP byte
     * @param toTest2 : third IP byte
     * @param toTest3 : fourth IP byte
     * @return true if all bytes are between 0 and 255; 0 otherwise
     */
    public static boolean checkIP(int toTest0, int toTest1, int toTest2, int toTest3)
    {
        boolean bResult     = false;
        if((toTest0 >=0 && toTest0 <256) && (toTest1 >=0 && toTest1 <256) && (toTest2 >=0 && toTest2 <256) && (toTest3 >=0 && toTest3 <256)){
            bResult         = true;
        }
        return bResult;
    }

    /**
     *
     * @param iAngleToTest
     * @return
     */
    public static boolean checkAngle(int iAngleToTest)
    {
        boolean bResult     = false;

        if((iAngleToTest >= -360) && (iAngleToTest <= 360))
        {
            bResult         = true;
        }
        return bResult;
    }

    public static int checkIntParam(String strParamToTest)
    {
        int iResult     = 0;

        if(strParamToTest == null)
        {
            iResult     = 0;
        }

        else
        {
            iResult     = Integer.parseInt(strParamToTest);
        }

        return iResult;
    }
}
