package com.app.rs_2016;

/**
 * Created by Sarion on 01/03/2015.
 */
public class CheckUserChoice {
    /**
     * @param toTest0 : first byte of the address
     * @param toTest1 : second byte of the address
     * @param toTest2 : third byte of the address
     * @param toTest3 : fourth byte of the address
     * @return : 1 if all bytes are between 0 and 255, 0 otherwise
     */
    public static boolean checkIP(int toTest0, int toTest1, int toTest2, int toTest3)
    {
        boolean bResult     = false;
        if((toTest0 >=0 && toTest0 <256) && (toTest1 >=0 && toTest1 <256) && (toTest2 >=0 && toTest2 <256) && (toTest3 >=0 && toTest3 <256)){
            bResult = true;
        }
        return bResult;
    }
}
