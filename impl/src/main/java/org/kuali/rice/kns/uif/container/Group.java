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
package org.kuali.rice.kns.uif.container;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.kns.uif.Component;
import org.kuali.rice.kns.uif.field.AttributeField;
import org.kuali.rice.kns.uif.field.Field;
import org.kuali.rice.kns.uif.field.GroupField;

/**
 * Container that holds a list of <code>Field</code> or other <code>Group</code>
 * instances
 * 
 * <p>
 * Groups can exist at different levels of the <code>View</code>, providing
 * conceptual groupings such as the page, section, and group. In addition, other
 * group types can be created to add behavior like collection support
 * </p>
 * 
 * <p>
 * <code>Group</code> implementation has properties for defaulting the binding
 * information (such as the parent object path and a binding prefix) for the
 * fields it contains. During the phase these properties (if given) are set on
 * the fields contained in the <code>Group</code> that implement
 * <code>DataBinding</code>, unless they have already been set on the field.
 * </p>
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public class Group extends ContainerBase {
	private static final long serialVersionUID = 7953641325356535509L;

	private String fieldBindByNamePrefix;
	private String fieldBindingObjectPath;

	private List<? extends Component> items;

	/**
	 * Default Constructor
	 */
	public Group() {
		items = new ArrayList<Component>();
	}

	/**
	 * <p>
	 * The following initialization is performed:
	 * <ul>
	 * <li>Sets the bindByNamePrefix if blank on any AttributeField and
	 * GroupField instances within the items List</li>
	 * </ul>
	 * </p>
	 * 
	 * @see org.kuali.rice.kns.uif.ComponentBase#performInitialization(org.kuali.rice.kns.uif.container.View)
	 */
	@Override
	public void performInitialization(View view) {
		super.performInitialization(view);

		for (Component component : getItems()) {
			// append group's field bind by name prefix (if set) to each
			// attribute field's binding prefix
			if (component instanceof AttributeField) {
				AttributeField attributeField = (AttributeField) component;

				if (StringUtils.isNotBlank(getFieldBindByNamePrefix())) {
					String bindByNamePrefixToSet = getFieldBindByNamePrefix();

					if (StringUtils.isNotBlank(attributeField.getBindingInfo().getBindByNamePrefix())) {
						bindByNamePrefixToSet += "." + attributeField.getBindingInfo().getBindByNamePrefix();
					}
					attributeField.getBindingInfo().setBindByNamePrefix(bindByNamePrefixToSet);
				}

				if (StringUtils.isNotBlank(fieldBindingObjectPath)
						&& StringUtils.isBlank(attributeField.getBindingInfo().getBindingObjectPath())) {
					attributeField.getBindingInfo().setBindingObjectPath(fieldBindingObjectPath);
				}
			}
			// set on GroupField's group to recursively set AttributeFields
			else if (component instanceof GroupField) {
				GroupField groupField = (GroupField) component;

				if (groupField.getGroup() != null) {
					if (StringUtils.isBlank(groupField.getGroup().getFieldBindByNamePrefix())) {
						groupField.getGroup().setFieldBindByNamePrefix(fieldBindByNamePrefix);
					}
					if (StringUtils.isBlank(groupField.getGroup().getFieldBindingObjectPath())) {
						groupField.getGroup().setFieldBindingObjectPath(fieldBindingObjectPath);
					}
				}
			}
		}
	}

	/**
	 * @see org.kuali.rice.krad.web.view.container.ContainerBase#getSupportedComponents()
	 */
	@Override
	public Set<Class<? extends Component>> getSupportedComponents() {
		Set<Class<? extends Component>> supportedComponents = new HashSet<Class<? extends Component>>();
		supportedComponents.add(Field.class);
		supportedComponents.add(Group.class);

		return supportedComponents;
	}

	/**
	 * @see org.kuali.rice.kns.uif.Component#getComponentTypeName()
	 */
	@Override
	public String getComponentTypeName() {
		return "group";
	}

	/**
	 * Binding prefix string to set on each of the groups
	 * <code>AttributeField</code> instances
	 * <p>
	 * As opposed to setting the bindingPrefix on each attribute field instance,
	 * it can be set here for the group. During initialize the string will then
	 * be set on each attribute field instance if the bindingPrefix is blank and
	 * not a form field
	 * </p>
	 * 
	 * @return String binding prefix to set
	 * @see org.kuali.rice.kns.uif.field.AttributeField#getBindByNamePrefix
	 */
	public String getFieldBindByNamePrefix() {
		return this.fieldBindByNamePrefix;
	}

	/**
	 * Setter for the field binding prefix
	 * 
	 * @param fieldBindByNamePrefix
	 */
	public void setFieldBindByNamePrefix(String fieldBindByNamePrefix) {
		this.fieldBindByNamePrefix = fieldBindByNamePrefix;
	}

	/**
	 * Object binding path to set on each of the group's
	 * <code>AttributeField</code> instances
	 * 
	 * <p>
	 * When the attributes of the group belong to a object whose path is
	 * different from the default then this property can be given to set each of
	 * the attributes instead of setting the model path on each one. The object
	 * path can be overridden at the attribute level. The object path is set to
	 * the fieldBindingObjectPath during the initialize phase.
	 * </p>
	 * 
	 * @return String model path to set
	 * @see org.kuali.rice.kns.uif.BindingInfo.getBindingObjectPath()
	 */
	public String getFieldBindingObjectPath() {
		return this.fieldBindingObjectPath;
	}

	/**
	 * Setter for the field object binding path
	 * 
	 * @param fieldBindingObjectPath
	 */
	public void setFieldBindingObjectPath(String fieldBindingObjectPath) {
		this.fieldBindingObjectPath = fieldBindingObjectPath;
	}

	/**
	 * @see org.kuali.rice.kns.uif.container.ContainerBase#getItems()
	 */
	@Override
	public List<? extends Component> getItems() {
		return this.items;
	}

	/**
	 * Setter for the Group's list of components
	 * 
	 * @param items
	 */
	public void setItems(List<? extends Component> items) {
		this.items = items;
	}

}
