package com.mms.thp.repository.impl;

import com.mms.thp.dto.SearchCriteria;
import com.mms.thp.model.Medicine;
import com.mms.thp.repository.MedicineExtendedRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class MedicineExtendedRepositoryImpl implements MedicineExtendedRepository {

    private static final String SEARCH_QUERY = "select m from Medicine m ";
    private static final String WHERE_NAME_CLAUSE = "where m.name like :name";
    private static final String WHERE_COMPANY_CLAUSE = " and m.company like :company ";
    private static final String WHERE_VOLUME_CLAUSE = " and m.volume = :volume";

    private static final String NAME_PARAM = "name";
    private static final String COMPANY_PARAM = "company";
    private static final String VOLUME_PARAM = "volume";

    private EntityManager entityManager;

    @Override
    public List<Medicine> searchMedicineByCriteria(SearchCriteria criteria) {

        boolean hasMedicineNameInCriteria = null != criteria.getMedicineName();
        boolean hasCompanyNameInCriteria = null != criteria.getCompanyName();
        boolean hasMedicineVolumeInCriteria = 0 != criteria.getMedicineVolume();

        StringBuilder query = new StringBuilder(SEARCH_QUERY);
        if(hasMedicineNameInCriteria)
            query.append(WHERE_NAME_CLAUSE);
        if(hasCompanyNameInCriteria)
            query.append(WHERE_COMPANY_CLAUSE);
        if(hasMedicineVolumeInCriteria)
            query.append(WHERE_VOLUME_CLAUSE);
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
