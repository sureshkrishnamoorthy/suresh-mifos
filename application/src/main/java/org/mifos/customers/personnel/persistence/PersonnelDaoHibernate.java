/*
 * Copyright (c) 2005-2010 Grameen Foundation USA
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * See also http://www.apache.org/licenses/LICENSE-2.0.html for an
 * explanation of the license and how it is applied.
 */

package org.mifos.customers.personnel.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.LogicalExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.mifos.accounts.savings.persistence.GenericDao;
import org.mifos.accounts.savings.persistence.GenericDaoHibernate;
import org.mifos.application.NamedQueryConstants;
import org.mifos.application.master.business.CustomFieldDefinitionEntity;
import org.mifos.application.master.util.helpers.MasterConstants;
import org.mifos.application.servicefacade.CenterCreation;
import org.mifos.application.util.helpers.EntityType;
import org.mifos.customers.personnel.business.PersonnelBO;
import org.mifos.customers.personnel.business.PersonnelCustomFieldEntity;
import org.mifos.customers.personnel.business.PersonnelDto;
import org.mifos.customers.personnel.business.PersonnelRoleEntity;
import org.mifos.customers.personnel.util.helpers.PersonnelLevel;
import org.mifos.customers.personnel.util.helpers.PersonnelStatus;
import org.mifos.dto.domain.UserDetailDto;
import org.mifos.dto.domain.UserSearchDto;
import org.mifos.dto.screen.SystemUserSearchResultsDto;
import org.mifos.security.MifosUser;
import org.mifos.security.rolesandpermission.business.RoleBO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

public class PersonnelDaoHibernate implements PersonnelDao {

    private final GenericDao genericDao;

    private static final ResourceBundle activityIdToRolesMap = ResourceBundle.getBundle("org.mifos.security.rolesandpermission.mifos_activity_role");

    public PersonnelDaoHibernate(GenericDao genericDao) {
        this.genericDao = genericDao;
    }

    @Override
    public void save(PersonnelBO user) {
        this.genericDao.createOrUpdate(user);
    }

    @Override
    public PersonnelBO findPersonnelById(Short id) {
        if (id == null) {
            return null;
        }

        HashMap<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("PERSONNEL_ID", id);

        return (PersonnelBO) this.genericDao.executeUniqueResultNamedQuery("findPersonnelById", queryParameters);
    }

    @Override
    public PersonnelBO findPersonnelByUsername(final String username) {
        HashMap<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("USER_NAME", username);

        return (PersonnelBO) this.genericDao.executeUniqueResultNamedQuery(NamedQueryConstants.GETPERSONNELBYNAME, queryParameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public MifosUser findAuthenticatedUserByUsername(String username) {
        PersonnelBO user = findPersonnelByUsername(username);
        if (user == null) {
            return null;
        }

        Set<Short> roleIds = new HashSet<Short>();
        for (PersonnelRoleEntity personnelRole : user.getPersonnelRoles()) {
            RoleBO role = personnelRole.getRole();
            roleIds.add(role.getId());
        }

        List<Short> activityIds = new ArrayList<Short>();
        if (!roleIds.isEmpty()) {
            HashMap<String, Object> queryParameters = new HashMap<String, Object>();
            queryParameters.put("ROLE_IDS", new ArrayList<Short>(roleIds));
            activityIds = (List<Short>) this.genericDao.executeNamedQuery("findDistinctActivityIdsForGivenSetOfRoleIds", queryParameters);
        }

        byte[] password = user.getEncryptedPassword();
        boolean enabled = user.isActive();
        boolean accountNonExpired = !user.isLocked();
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = !user.isLocked();

        List<GrantedAuthority> authorities = getGrantedActivityAuthorities(activityIds);

        return new MifosUser(user.getPersonnelId(), user.getOffice().getOfficeId(), username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<PersonnelDto> findActiveLoanOfficersForOffice(CenterCreation centerCreationDto) {

        HashMap<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("levelId", PersonnelLevel.LOAN_OFFICER.getValue());
        queryParameters.put("userId", centerCreationDto.getLoggedInUserId());
        queryParameters.put("userLevelId", centerCreationDto.getLoggedInUserLevelId());
        queryParameters.put("officeId", centerCreationDto.getOfficeId());
        queryParameters.put("statusId", PersonnelStatus.ACTIVE.getValue());

        List<PersonnelDto> queryResult = (List<PersonnelDto>) genericDao.executeNamedQuery(
                NamedQueryConstants.MASTERDATA_ACTIVE_LOANOFFICERS_INBRANCH, queryParameters);

        return queryResult;
    }

    @Override
    public PersonnelBO findByGlobalPersonnelNum(String globalNumber) {

        HashMap<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("globalPersonnelNum", globalNumber);

        return (PersonnelBO) this.genericDao.executeUniqueResultNamedQuery(NamedQueryConstants.PERSONNEL_BY_SYSTEM_ID, queryParameters);
    }

    private List<GrantedAuthority> getGrantedActivityAuthorities(List<Short> activityIds) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for(Short activityId : activityIds) {
            authorities.add(new GrantedAuthorityImpl(getRoleForActivityId(activityId.toString())));
        }
        return authorities;
    }

    private String getRoleForActivityId(String activityId) {
        try {
            return activityIdToRolesMap.getString(activityId);
        } catch (MissingResourceException e) {
            return "ROLE_UNDEFINED";
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Iterator<CustomFieldDefinitionEntity> retrieveCustomFieldEntitiesForPersonnel() {
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put(MasterConstants.ENTITY_TYPE, EntityType.PERSONNEL.getValue());
        return (Iterator<CustomFieldDefinitionEntity>) genericDao.executeNamedQueryIterator(NamedQueryConstants.RETRIEVE_CUSTOM_FIELDS, queryParameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Iterator<PersonnelCustomFieldEntity> getCustomFieldResponses(Short customFieldId) {
        Map<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("CUSTOM_FIELD_ID", customFieldId);
        return (Iterator<PersonnelCustomFieldEntity>) genericDao.executeNamedQueryIterator("PersonnelCustomFieldEntity.getResponses", queryParameters);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SystemUserSearchResultsDto search(UserSearchDto searchDto, MifosUser user) {

        Short userId = Integer.valueOf(user.getUserId()).shortValue();

        PersonnelBO loggedInUser = findPersonnelById(userId);

        final PersonnelLevel level = loggedInUser.getLevelEnum();
        final String searchAllSubOfficesInclusiveOfLoggedInUserOffice = loggedInUser.getOfficeSearchId() + "%";
        final String searchString = org.mifos.framework.util.helpers.SearchUtils.normalizeSearchString(searchDto.getSearchTerm());
        final String username = searchString + "%";
        String firstName = "";
        String secondPartOfName = "";

        HashMap<String, Object> queryParameters = new HashMap<String, Object>();
        queryParameters.put("SEARCH_ALL", searchAllSubOfficesInclusiveOfLoggedInUserOffice);
        queryParameters.put("USERID", userId);
        queryParameters.put("LOID", PersonnelLevel.LOAN_OFFICER.getValue());
        queryParameters.put("USERLEVEL_ID", level.getValue());
        queryParameters.put("USER_NAME", username);

        if (searchString.contains(" ")) {
            firstName = searchString.substring(0, searchString.indexOf(" "));
            secondPartOfName = searchString.substring(searchString.indexOf(" ") + 1,searchString.length());
            queryParameters.put("USER_NAME1", firstName);
            queryParameters.put("USER_NAME2", secondPartOfName);
        } else {
            firstName = searchString;
            secondPartOfName = "";
            queryParameters.put("USER_NAME1", searchString);
            queryParameters.put("USER_NAME2", "");
        }

        Long searchResultsCount = (Long) this.genericDao.executeUniqueResultNamedQuery(NamedQueryConstants.PERSONNEL_SEARCH_COUNT, queryParameters);

        Session session = ((GenericDaoHibernate) this.genericDao).getHibernateUtil().getSessionTL();
        Criteria criteriaQuery = session.createCriteria(PersonnelBO.class);
        criteriaQuery.createAlias("office", "o");
        criteriaQuery.createAlias("personnelDetails", "d");

        if (PersonnelLevel.LOAN_OFFICER.getValue().equals(Short.valueOf("2"))) {
            criteriaQuery.add(Restrictions.eq("personnelId", userId));
        }
        criteriaQuery.add(Restrictions.like("o.searchId", searchAllSubOfficesInclusiveOfLoggedInUserOffice));

        LogicalExpression firstOrLastNameMatchUsername =  Restrictions.or(Restrictions.like("d.name.firstName", username), Restrictions.like("d.name.lastName", username));
        LogicalExpression firstNameAndLastNameMatchGivenParts = Restrictions.and(Restrictions.like("d.name.firstName", firstName), Restrictions.like("d.name.lastName", secondPartOfName));

        criteriaQuery.add(Restrictions.or(firstOrLastNameMatchUsername, firstNameAndLastNameMatchGivenParts));
        criteriaQuery.addOrder(Order.asc("o.officeName"));
        criteriaQuery.addOrder(Order.asc("d.name.lastName"));

        criteriaQuery.setFetchMode("office", FetchMode.JOIN);
        criteriaQuery.setFetchMode("level", FetchMode.JOIN);
        criteriaQuery.setFetchMode("personnelDetails", FetchMode.JOIN);

        int firstResult = (searchDto.getPage() * searchDto.getPageSize()) - searchDto.getPageSize();
        criteriaQuery.setFirstResult(firstResult);
        criteriaQuery.setMaxResults(searchDto.getPageSize());

        List<PersonnelBO> pagedResults = criteriaQuery.list();
        List<UserDetailDto> pagedUserDetails = new ArrayList<UserDetailDto>();
        for (PersonnelBO personnelBO : pagedResults) {
            pagedUserDetails.add(personnelBO.toDto());
        }

        SystemUserSearchResultsDto resultsDto = new SystemUserSearchResultsDto(searchResultsCount.intValue(), firstResult, searchDto.getPage(), searchDto.getPageSize(), pagedUserDetails);

        return resultsDto;
    }
}