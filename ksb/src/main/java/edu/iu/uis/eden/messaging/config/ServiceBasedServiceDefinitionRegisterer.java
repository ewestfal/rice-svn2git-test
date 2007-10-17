/*
 * Copyright 2007 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.iu.uis.eden.messaging.config;

import org.kuali.bus.services.KSBServiceLocator;
import org.kuali.rice.resourceloader.GlobalResourceLoader;

import edu.iu.uis.eden.messaging.ServiceDefinition;

/**
 * Registers {@link ServiceDefinition} objects 
 * configured as a service (typically a Spring bean) dynamically with the 
 * service registry.
 *  
 *  
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public class ServiceBasedServiceDefinitionRegisterer {
	
	private String serviceName;
	
	public ServiceBasedServiceDefinitionRegisterer(String serviceName) {
		this.setServiceName(serviceName);
	}

	/**
	 * Goes to the {@link GlobalResourceLoader} to find the ServiceDefinition using the name
	 * passed in.  Validates the Definition and registers it with the registry.
	 * 
	 * @param serviceName
	 * @param forceRegistryRefresh
	 */
	public void registerServiceDefinition(boolean forceRegistryRefresh) {
		ServiceDefinition serviceDef = (ServiceDefinition)GlobalResourceLoader.getService(this.getServiceName());
		serviceDef.validate();
		KSBServiceLocator.getServiceDeployer().registerService(serviceDef, forceRegistryRefresh);
	}
	
	public void unregisterServiceDefinition() {
		ServiceDefinition serviceDef = (ServiceDefinition)GlobalResourceLoader.getService(this.getServiceName());
		KSBServiceLocator.getServiceDeployer().removeRemoteServiceFromRegistry(serviceDef.getServiceName());
	}

	public String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}