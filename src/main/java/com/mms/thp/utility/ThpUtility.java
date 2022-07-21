package com.mms.thp.utility;

import org.springframework.ui.Model;

public class ThpUtility {

    public static final String ADMIN_KEY = "NO-ONE-CAN-CHANGE";
    public static final int RECORD_PER_PAGE = 15;

    private ThpUtility(){/* No constructor for utility class */}
    public static String normalizeString(String arg) {
        return arg.trim().replaceAll("\\s", "").toLowerCase();
    }
    public static String normalizeStringForDisplaying(String arg) {
        return arg.trim().toUpperCase();
    }
    public static void populateModelForPagination(Model model, long totalMedicineCount, int pageNo, String url){

        double totalPages = (double)totalMedicineCount/RECORD_PER_PAGE;
        int totalIntCount = (int)totalMedicineCount/RECORD_PER_PAGE;
        if(totalPages > totalIntCount)
            totalPages+=1;
        boolean hasNextPage = false;
        boolean hasPreviousPage = false;
        String nextPageLink = null;
        String previousPageLink = null;
        {
            int pageNoTemp = pageNo+1;
            if((int)totalPages > pageNoTemp){
                hasNextPage = true;
                nextPageLink = url+"pageNo="+pageNoTemp;
                previousPageLink = url+"pageNo="+pageNo;
            }
            if(pageNo > 0) {
                hasPreviousPage = true;
                previousPageLink = url+"pageNo="+(pageNo-1);
            }
            if((int)totalPages == pageNoTemp) {
                hasNextPage = false;
            }
        }
        model.addAttribute("totalPages", (int)totalPages);
        model.addAttribute("nextPageLink", nextPageLink);
        model.addAttribute("previousPageLink", previousPageLink);
        model.addAttribute("hasNextPage", hasNextPage);
        model.addAttribute("hasPreviousPage", hasPreviousPage);

    }
    public enum MedicineColumnIndex {
        NAME,
        COMPANY,
        PRICE,
        COUNT,
        ML,
        BOX_NUMBER
    }
}
