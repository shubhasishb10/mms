package com.mms.thp.repository.impl;

import com.mms.thp.dto.SearchCriteria;
import com.mms.thp.model.Medicine;
import com.mms.thp.repository.MedicineExtendedRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class MedicineExtendedRepositoryImpl implements MedicineExtendedRepository {

    private static final String CONJUNCTION_STRING = " and ";
    private static final String SEARCH_QUERY = "select m from Medicine m where";
    private static final String WHERE_NAME_CLAUSE = " m.name like :name";
    private static final String WHERE_COMPANY_CLAUSE = " m.companyStringName like :companyStringName ";
    private static final String WHERE_VOLUME_CLAUSE = " m.volume = :volume";
    private static final String SORT_MEDICINE_ASC_QUERY = " order by m.name asc";

    private static final String NAME_PARAM = "name";
    private static final String COMPANY_PARAM = "companyStringName";
    private static final String VOLUME_PARAM = "volume";

    private EntityManager entityManager;

    @Override
    public List<Medicine> searchMedicineByCriteria(SearchCriteria criteria) {

        boolean hasMedicineNameInCriteria = null != criteria.getMedicineName() && !criteria.getMedicineName().equalsIgnoreCase("");
        boolean hasCompanyNameInCriteria = null != criteria.getCompanyName() && !criteria.getCompanyName().equalsIgnoreCase("");
        boolean hasMedicineVolumeInCriteria = 0 != criteria.getMedicineVolume();

        StringBuilder query = new StringBuilder(SEARCH_QUERY);
        if(hasMedicineNameInCriteria) {
            query.append(WHERE_NAME_CLAUSE);
        }
        if(hasCompanyNameInCriteria) {
            if(SearchCriteria.hasPreviousParam(criteria, SearchCriteria.PARAM_NAME_COMPANY)){
                query.append(CONJUNCTION_STRING);
            }
            query.append(WHERE_COMPANY_CLAUSE);
        }
        if(hasMedicineVolumeInCriteria) {
            if(SearchCriteria.hasPreviousParam(criteria, SearchCriteria.PARAM_NAME_ML)){
                query.append(CONJUNCTION_STRING);
            }
            query.append(WHERE_VOLUME_CLAUSE);
        }
        query.append(SORT_MEDICINE_ASC_QUERY);
        TypedQuery<Medicine> medicineListQuery = entityManager.createQuery(query.toString(), Medicine.class);
        if(hasMedicineNameInCriteria)
            medicineListQuery.setParameter(NAME_PARAM, "%"+criteria.getMedicineName()+"%");
        if(hasCompanyNameInCriteria)
            medicineListQuery.setParameter(COMPANY_PARAM, "%"+criteria.getCompanyName()+"%");
        if(hasMedicineVolumeInCriteria)
            medicineListQuery.setParameter(VOLUME_PARAM, criteria.getMedicineVolume());
        return medicineListQuery.getResultList();
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
