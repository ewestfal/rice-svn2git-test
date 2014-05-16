/*
 * Copyright 2006-2012 The Kuali Foundation
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
package edu.samplu.krad.travelview;

import edu.samplu.common.KradMenuITBase;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class MaintenanceAdHocRecipientsIT extends KradMenuITBase {

    @Override
    protected String getLinkLocator() {
        return "link=Travel Account Maintenance (New)";
    }

    @Test
    /**
     * Verify the Ad Hoc Recipients section and fields
     */
    public void testVerifyAdHocRecipients() throws Exception {
        gotoMenuLinkLocator();
        waitAndClick("css=a > span:contains(Fiscal Officer Accounts)");
//        waitAndClick("css=#u416_toggle > span.uif-headerText-span");  // id is generated and has changed
//        for (int second = 0;; second++) {
//            if (second >= 15) {
//                fail("timeout");
//            }
//
//            if (isElementPresent("css=#u440 > h4.uif-headerText > span.uif-headerText-span")) {
//                break;
//            }
//
//            Thread.sleep(1000);
//        }

        assertTrue(isElementPresent("//select[@name=\"newCollectionLines['document.adHocRoutePersons'].actionRequested\"]"));
        assertTrue(isElementPresent("//input[@name=\"newCollectionLines['document.adHocRoutePersons'].name\" and @type=\"text\"]"));
//        assertTrue(isElementPresent("css=#u551_add")); // id is generated and has changed
        assertTrue(isElementPresent("//select[@name=\"newCollectionLines['document.adHocRouteWorkgroups'].actionRequested\"]"));
        assertTrue(isElementPresent("//input[@name=\"newCollectionLines['document.adHocRouteWorkgroups'].recipientNamespaceCode\" and @type='text']"));
        assertTrue(isElementPresent("//input[@name=\"newCollectionLines['document.adHocRouteWorkgroups'].recipientName\" and @type='text']"));
//        assertTrue(isElementPresent("css=#u700_add")); // id is generated and has changed
    }
}