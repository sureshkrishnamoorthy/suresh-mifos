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
package org.mifos.customers.office.business.service;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.mifos.customers.center.struts.action.OfficeHierarchyDto;
import org.mifos.framework.exceptions.ServiceException;

import edu.emory.mathcs.backport.java.util.Collections;

public class OfficeFacade {

    private OfficeBusinessService officeBusinessService;

    public OfficeFacade(OfficeBusinessService officeBusinessService) {
        this.officeBusinessService = officeBusinessService;
    }

    public OfficeHierarchyDto headOfficeHierarchy() throws ServiceException {
        return officeBusinessService.headOfficeHierarchy();
    }

    public String officeNames(String ids) {
        String[] idArray = ids.split(",");
        List<Short> idList = new LinkedList<Short>();
        for (String id : idArray) {
            idList.add(new Short(id));
        }
        List<String> topLevelOffices = officeBusinessService.officeNames(idList);
        StringBuffer stringBuffer = new StringBuffer();
        for (Iterator<String> iterator = topLevelOffices.iterator(); iterator.hasNext();) {
            stringBuffer.append(iterator.next());
            if (iterator.hasNext()) {
                stringBuffer.append(", ");
            }
        }
        return stringBuffer.toString();
    }
}
