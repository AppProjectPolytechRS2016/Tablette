package com.app.rs_2016;


/**
 * Created by Sarion on 01/03/2015.
 */


public class CheckUserChoice {

    /**
     *@function : checkIP
     * @param toTest0 : first IP byte
     * @param toTest1 : second IP byte
     * @param toTest2 : third IP byte
     * @param toTest3 : fourth IP byte
     * @description : check each byte of an IP address
     * @return true if all bytes are between 0 and 255; 0 otherwise
     */
    public static boolean checkIP(int toTest0, int toTest1, int toTest2, int toTest3)
    {
        //Declarations
        boolean bResult     = false;

        //Check if each byte is between 0 and 255
        if((toTest0 >=0 && toTest0 <256) && (toTest1 >=0 && toTest1 <256) && (toTest2 >=0 && toTest2 <256) && (toTest3 >=0 && toTest3 <256)){
            bResult         = true;
        }

        return bResult;
    }


    /**
     * @function : checkIntParam
     * @param strParamToTest
     * @description : check and convert the string to an integer
     * @return
     */
    public static int checkIntParam(String strParamToTest)
    {
        //Declarations
        int iResult     = 0;

        //Set the default value if the parameter is null
        if(strParamToTest == null)
        {
            iResult     = 0;
        }

        //Convert the recovered String into an integer
        else
        {
            iResult     = Integer.parseInt(strParamToTest);
        }

        return iResult;
    }

    /**
     * @function : recoverOrderName
     * @param strText
     * @description : recover the order name
     * @return
     */
    public static String recoverOrderName(String strText){
        //Declarations
        String[] splitArray = null;
        String orderName    = null;

        //Parse the string; delimiter = " "
        splitArray = strText.split(" ");
        orderName = splitArray[0];

        return orderName;
    }

}
