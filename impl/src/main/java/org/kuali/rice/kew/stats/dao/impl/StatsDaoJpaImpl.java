/*
 * Copyright 2007-2008 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.stats.dao.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.ojb.broker.accesslayer.LookupException;
import org.kuali.rice.kew.stats.Stats;
import org.kuali.rice.kew.stats.dao.StatsDAO;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kew.web.KeyValue;

/**
 * This is a description of what this class does - ddean don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
// There isn't an obvious place to put these @NamedQueries since they are just doing select count(*) from various tables.
// Thus I'm using the query literals in this class, move these NamedQuerys to wherever they need to go.
// @NamedQueries({
//    @NamedQuery(name="Stats.DocumentsRoutedReport",  query="select count(*) as count, drhv.docRouteStatus from DocumentRouteHeaderValue drhv where drhv.createDate between :beginDate and :endDate group by docRouteStatus"),
//    @NamedQuery(name="Stats.NumActiveItemsReport",  query="select count(*) from ActionItem ai"),
//    @NamedQuery(name="Stats.NumInitiatedDocsByDocTypeReport",  query="select count(*), dt.name from DocumentRouteHeaderValue drhv, DocumentType dt where drhv.createDate > :createDate and drhv.documentTypeId = dt.documentTypeId group by dt.name"),
//    @NamedQuery(name="Stats.NumUsersReport",  query="select count(distinct workflowId) from UserOptions uo"),
//    @NamedQuery(name="Stats.NumberOfDocTypesReport",  query="select count(*) from DocumentType dt where dt.currentInd = true")
//  })
public class StatsDaoJpaImpl implements StatsDAO {

    @PersistenceContext
    private EntityManager entityManager;
    
    public void DocumentsRoutedReport(Stats stats, Date begDate, Date endDate) throws SQLException, LookupException {
        Query query = entityManager.createQuery("select count(*) as count, drhv.docRouteStatus from DocumentRouteHeaderValue drhv where drhv.createDate between :beginDate and :endDate group by docRouteStatus");
//        Query query = entityManager.createNamedQuery("Stats.DocumentsRoutedReport");
        query.setParameter("beginDate", begDate.getTime());
        query.setParameter("endDate", endDate.getTime());
        
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        
        for (Object[] result : resultList) {
            String actionType = result[1].toString();
            String number = result[0].toString();
            if (actionType.equals(KEWConstants.ROUTE_HEADER_APPROVED_CD)) {
                stats.setApprovedNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_CANCEL_CD)) {
                stats.setCanceledNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_DISAPPROVED_CD)) {
                stats.setDisapprovedNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_ENROUTE_CD)) {
                stats.setEnrouteNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_EXCEPTION_CD)) {
                stats.setExceptionNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_FINAL_CD)) {
                stats.setFinalNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_INITIATED_CD)) {
                stats.setInitiatedNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_PROCESSED_CD)) {
                stats.setProcessedNumber(number);
            } else if (actionType.equals(KEWConstants.ROUTE_HEADER_SAVED_CD)) {
                stats.setSavedNumber(number);
            }
        }
    }

    public void NumActiveItemsReport(Stats stats) throws SQLException, LookupException {
        stats.setNumActionItems(entityManager.createNamedQuery("select count(*) from ActionItem ai").getSingleResult().toString());
//        stats.setNumActionItems(entityManager.createNamedQuery("Stats.NumActiveItemsReport").getSingleResult().toString());
    }

    public void NumInitiatedDocsByDocTypeReport(Stats stats) throws SQLException, LookupException {
        Query query = entityManager.createNamedQuery("select count(*), dt.name from DocumentRouteHeaderValue drhv, DocumentType dt where drhv.createDate > :createDate and drhv.documentTypeId = dt.documentTypeId group by dt.name");
//        Query query = entityManager.createNamedQuery("Stats.NumInitiatedDocsByDocTypeReport");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -29);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);        
        query.setParameter("createDate", new Timestamp(calendar.getTime().getTime()));
        
        @SuppressWarnings("unchecked")
        List<Object[]> resultList = query.getResultList();
        
        List<KeyValue> numDocs = new ArrayList<KeyValue>(resultList.size());
        for (Object[] result : resultList) {
            numDocs.add(new KeyValue(result[1].toString(),result[0].toString()));
        }
        
        stats.setNumInitiatedDocsByDocType(numDocs);
    }

    public void NumUsersReport(Stats stats) throws SQLException, LookupException {
        stats.setNumUsers(entityManager.createNamedQuery("select count(distinct workflowId) from UserOptions uo").getSingleResult().toString());
//        stats.setNumUsers(entityManager.createNamedQuery("Stats.NumUsersReport").getSingleResult().toString());
    }

    public void NumberOfDocTypesReport(Stats stats) throws SQLException, LookupException {
        stats.setNumDocTypes(entityManager.createNamedQuery("select count(*) from DocumentType dt where dt.currentInd = true").getSingleResult().toString());
//        stats.setNumDocTypes(entityManager.createNamedQuery("Stats.NumberOfDocTypesReport").getSingleResult().toString());
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

}