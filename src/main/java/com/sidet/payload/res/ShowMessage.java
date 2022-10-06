package com.sidet.payload.res;

public class ShowMessage {

    public static String success(String title, String operation){
        return title + " has been " + operation + " successfully !";
    }

    public static String pagination(int totalPages, int pageNo){
        return  "The total pages has only " + totalPages +" and your current page is "+ pageNo;
    }

}
