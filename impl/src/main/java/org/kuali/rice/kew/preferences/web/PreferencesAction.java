/*
 * Copyright 2005-2007 The Kuali Foundation
 *
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
package org.kuali.rice.kew.preferences.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessages;
import org.kuali.rice.core.config.ConfigContext;
import org.kuali.rice.core.util.JSTLConstants;
import org.kuali.rice.kew.preferences.Preferences;
import org.kuali.rice.kew.preferences.service.PreferencesService;
import org.kuali.rice.kew.service.KEWServiceLocator;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kew.web.KewKualiAction;
import org.kuali.rice.kew.web.KeyValue;
import org.kuali.rice.kew.web.session.UserSession;
import org.kuali.rice.kns.util.GlobalVariables;


/**
 * A Struts Action for interfaces with {@link Preferences}.
 *
 * @see PreferencesService
 * @see Preferences
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class PreferencesAction extends KewKualiAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        initForm(request, form);
        request.setAttribute("Constants", new JSTLConstants(KEWConstants.class));
        return super.execute(mapping, form, request, response);
    }

    public ActionForward start(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PreferencesService preferencesService = (PreferencesService) KEWServiceLocator.getService(KEWServiceLocator.PREFERENCES_SERVICE);
        PreferencesForm preferencesForm = (PreferencesForm) form;
        preferencesForm.setPreferences(preferencesService.getPreferences(getUserSession(request).getPrincipalId()));
        return mapping.findForward("basic");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        PreferencesService prefSrv = (PreferencesService) KEWServiceLocator.getService(KEWServiceLocator.PREFERENCES_SERVICE);
        PreferencesForm prefForm = (PreferencesForm) form;

        prefForm.validatePreferences();
        if (GlobalVariables.getMessageMap().hasNoErrors()) {
            prefSrv.savePreferences(getUserSession(request).getPrincipalId(), prefForm.getPreferences());
        }
        getUserSession(request).refreshPreferences();
        if (! StringUtils.isEmpty(prefForm.getReturnMapping())) {
            return mapping.findForward(prefForm.getReturnMapping());
        }
        return mapping.findForward("basic");
    }

    public ActionMessages initForm(HttpServletRequest request, ActionForm form) throws Exception {
        request.setAttribute("actionListContent", KEWConstants.ACTION_LIST_CONTENT);
        getDelegatorFilterChoices(request);
        getPrimaryDelegateFilterChoices(request);
        PreferencesForm prefForm = (PreferencesForm)form;
        prefForm.setShowOutbox(ConfigContext.getCurrentContextConfig().getOutBoxOn());
        return null;
    }

    public void getDelegatorFilterChoices(HttpServletRequest request) {
        List delegatorFilterChoices = new ArrayList();
        delegatorFilterChoices.add(new KeyValue(KEWConstants.DELEGATORS_ON_FILTER_PAGE, KEWConstants.DELEGATORS_ON_FILTER_PAGE));
        delegatorFilterChoices.add(new KeyValue(KEWConstants.DELEGATORS_ON_ACTION_LIST_PAGE, KEWConstants.DELEGATORS_ON_ACTION_LIST_PAGE));
        request.setAttribute("delegatorFilter", delegatorFilterChoices);
    }
    
    public void getPrimaryDelegateFilterChoices(HttpServletRequest request) {
    	List<KeyValue> primaryDelegateFilterChoices = new ArrayList<KeyValue>();
    	primaryDelegateFilterChoices.add(new KeyValue(KEWConstants.PRIMARY_DELEGATES_ON_FILTER_PAGE, KEWConstants.PRIMARY_DELEGATES_ON_FILTER_PAGE));
        primaryDelegateFilterChoices.add(new KeyValue(KEWConstants.PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE, KEWConstants.PRIMARY_DELEGATES_ON_ACTION_LIST_PAGE));
        request.setAttribute("primaryDelegateFilter", primaryDelegateFilterChoices);
    }

    private static UserSession getUserSession(HttpServletRequest request) {
        return UserSession.getAuthenticatedUser();
    }
}