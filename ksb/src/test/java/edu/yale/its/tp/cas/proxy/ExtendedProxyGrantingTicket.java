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
package edu.yale.its.tp.cas.proxy;

import java.io.IOException;

/**
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 * @since 0.9
 *
 */
public class ExtendedProxyGrantingTicket extends ProxyGrantingTicket {

	public ExtendedProxyGrantingTicket(String arg0, String arg1) {
		super(arg0, arg1);
	}

	@Override
	public String getProxyTicket(final String target) throws IOException {
		return "PT";
	}
}