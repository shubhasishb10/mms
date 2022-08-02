package com.mms.thp.dto;

import com.mms.thp.utility.ThpUtility;

public class SearchCriteria {

    private String medicineName;
    private String companyName;
    private int medicineVolume;

    private SearchCriteria(){/*Private Constructor*/}

    public static SearchCriteriaBuilder builder(){
        return new SearchCriteriaBuilder();
    }

    public String getMedicineName() {
        return medicineName;
    }


    public String getCompanyName() {
        return companyName;
    }

    public int getMedicineVolume() {
        return medicineVolume;
    }

    public static class SearchCriteriaBuilder {
        private String medicineName;
        private String companyName;
        private int medicineVolume;
        private SearchCriteriaBuilder(){
            this.medicineName = "";
            this.companyName = "";
            this.medicineVolume = 0;
        }
        public void withMedicineName(String medicineName) {
            this.medicineName = ThpUtility.normalizeString(medicineName);
        }
        public void withCompanyName(String companyName) {
            this.companyName = ThpUtility.normalizeString(companyName);
        }
        public void withMedicineVolume(int medicineVolume){
            this.medicineVolume = medicineVolume;
        }
        public SearchCriteria buildDto(){
            SearchCriteria criteria = new SearchCriteria();
            if(null!=this.medicineName)
                criteria.medicineName = this.medicineName;
            if(null!=this.companyName)
                criteria.companyName = this.companyName;
            if(0 != this.medicineVolume)
                criteria.medicineVolume = this.medicineVolume;
            return criteria;
        }
    }

    public static boolean hasPreviousParam(SearchCriteria criteria, String nextParamName){

        switch(nextParamName) {
            case PARAM_NAME_COMPANY:
                return criteria.medicineName != null && !criteria.medicineName.equalsIgnoreCase("");
            case PARAM_NAME_ML:
                return (criteria.companyName != null && !criteria.companyName.equalsIgnoreCase(""))
                        || (criteria.medicineName != null && !criteria.medicineName.equalsIgnoreCase(""));
        }
        return Boolean.FALSE;
    }

    public static final String PARAM_NAME_COMPANY = "COMPANY";
    public static final String PARAM_NAME_ML = "ML";
}
