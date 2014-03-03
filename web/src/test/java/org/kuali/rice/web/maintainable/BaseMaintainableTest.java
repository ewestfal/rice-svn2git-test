/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.rice.web.maintainable;

import org.junit.Ignore;
import org.junit.Test;
import org.kuali.rice.test.web.HtmlUnitUtil;
import org.kuali.rice.web.test.ServerTestBase;

import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Ignore("KULRICE-3011")
public class BaseMaintainableTest extends ServerTestBase {
    

    @Test public void testMaintainable() throws Exception {
        HtmlPage maintPage = HtmlUnitUtil.gotoPageAndLogin(HtmlUnitUtil.BASE_URL + "/kr/maintenance.do?businessObjectClassName=edu.sampleu.travel.bo.TravelAccount&methodToCall=start#topOfForm");        
        HtmlForm maintForm = (HtmlForm)maintPage.getFormByName("KualiForm");
        maintForm.getInputByName("document.documentHeader.documentDescription").setValueAttribute("description");
        maintForm.getInputByName("document.newMaintainableObject.number").setValueAttribute("a6");
        maintForm.getInputByName("document.newMaintainableObject.name").setValueAttribute("myaccount");
        maintForm.getInputByName("document.newMaintainableObject.foId").setValueAttribute("1");
        maintForm.getSelectByName("document.newMaintainableObject.extension.accountTypeCode").setSelectedAttribute("CAT", true);
        HtmlPage returnPage = (HtmlPage)((HtmlInput)maintForm.getInputByName("methodToCall.route")).click();
        assertTrue("Document not successfully routed", HtmlUnitUtil.pageContainsText(returnPage, "Document was successfully submitted"));
    }
}