package com.example.demo.dto;

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
        private SearchCriteriaBuilder(){/*Private Constructor*/}
        public SearchCriteriaBuilder withMedicineName(String medicineName) {
            this.medicineName = medicineName;
            return this;
        }
        public SearchCriteriaBuilder withCompanyName(String companyName) {
            this.companyName = companyName;
            return this;
        }
        public SearchCriteriaBuilder withMedicineVolume(int medicineVolume){
            this.medicineVolume = medicineVolume;
            return this;
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
}
