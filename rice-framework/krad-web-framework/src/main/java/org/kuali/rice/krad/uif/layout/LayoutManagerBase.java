/**
 * Copyright 2005-2014 The Kuali Foundation
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
package org.kuali.rice.krad.uif.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;
import org.kuali.rice.krad.datadictionary.Copyable;
import org.kuali.rice.krad.datadictionary.parse.BeanTagAttribute;
import org.kuali.rice.krad.datadictionary.uif.UifDictionaryBeanBase;
import org.kuali.rice.krad.uif.UifConstants;
import org.kuali.rice.krad.uif.UifConstants.ViewStatus;
import org.kuali.rice.krad.uif.component.Component;
import org.kuali.rice.krad.uif.component.PropertyReplacer;
import org.kuali.rice.krad.uif.component.ReferenceCopy;
import org.kuali.rice.krad.uif.container.Container;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecycle;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecyclePhase;
import org.kuali.rice.krad.uif.lifecycle.ViewLifecycleTask;
import org.kuali.rice.krad.uif.util.LifecycleAwareList;
import org.kuali.rice.krad.uif.util.LifecycleAwareMap;
import org.kuali.rice.krad.uif.util.LifecycleElement;
import org.kuali.rice.krad.uif.view.View;

/**
 * Base class for all layout managers
 *
 * <p>
 * Provides general properties of all layout managers, such as the unique id,
 * rendering template, and style settings
 * </p>
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public abstract class LayoutManagerBase extends UifDictionaryBeanBase implements LayoutManager {
    private static final long serialVersionUID = -2657663560459456814L;

    private String id;
    private String viewPath;
    private Map<String, String> phasePathMapping;

    private String template;
    private String templateName;

    private String style;
    
    private List<String> libraryCssClasses;
    private List<String> cssClasses;
    private List<String> additionalCssClasses;

    @ReferenceCopy(newCollectionInstance = true)
    private Map<String, Object> context;

    private List<PropertyReplacer> propertyReplacers;
    
    private String viewStatus = UifConstants.ViewStatus.CREATED;

    public LayoutManagerBase() {
        super();

        phasePathMapping = new HashMap<String, String>();
        context = Collections.emptyMap();
        cssClasses = Collections.emptyList();
        libraryCssClasses = Collections.emptyList();
        additionalCssClasses = Collections.emptyList();
    }

    /**
     * @see LifecycleElement#checkMutable(boolean)
     */
    public void checkMutable(boolean legalDuringInitialization) {
        if (UifConstants.ViewStatus.CACHED.equals(viewStatus)) {
            ViewLifecycle.reportIllegalState("Cached layout manager " + getClass() + " " + getId()
                    + " is immutable, use copy() to get a mutable instance");
            return;
        }

        if (ViewLifecycle.isActive()) {
            return;
        }

        if (UifConstants.ViewStatus.CREATED.equals(viewStatus)) {
            if (!legalDuringInitialization) {
                ViewLifecycle.reportIllegalState(
                        "View has not been fully initialized, attempting to change layout manager "
                                + getClass() + " " + getId());
                return;
            }
        } else {
            ViewLifecycle.reportIllegalState("Layout manager " + getClass() + " " + getId()
                    + " has been initialized, but the lifecycle is not active.");
            return;
        }
    }

    /**
     * @see LifecycleElement#isMutable(boolean)
     */
    public boolean isMutable(boolean legalDuringInitialization) {
        return (UifConstants.ViewStatus.CREATED.equals(viewStatus) && legalDuringInitialization)
                || ViewLifecycle.isActive();
    }

    /**
     * Indicates what lifecycle phase the layout manager instance is in
     * 
     * <p>
     * The view lifecycle begins with the CREATED status. In this status a new instance of the view
     * has been retrieved from the dictionary, but no further processing has been done. After the
     * initialize phase has been run the status changes to INITIALIZED. After the model has been
     * applied and the view is ready for render the status changes to FINAL
     * </p>
     * 
     * @return view status
     * @see org.kuali.rice.krad.uif.UifConstants.ViewStatus
     */
    public String getViewStatus() {
        return this.viewStatus;
    }

    /**
     * Setter for the view status, invoked upon completion of a lifecycle phase.
     * 
     * @param phase The lifecycle phase that has just been completed.
     * @see ViewLifecyclePhase#getEndViewStatus()
     */
    @Override
    public void setViewStatus(ViewLifecyclePhase phase) {
        if (!viewStatus.equals(phase.getStartViewStatus()) &&
                !viewStatus.equals(phase.getEndViewStatus())) {
            ViewLifecycle.reportIllegalState("Component " + getClass().getName() + " is not in expected status "
                    + phase.getStartViewStatus() + " marking the completion of a lifecycle phase, found " + viewStatus
                    + "\nPhase: " + phase);
        }

        this.viewStatus = phase.getEndViewStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyCompleted(ViewLifecyclePhase phase) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performInitialization(Object model) {
        checkMutable(false);
        
        // set id of layout manager from container
        if (StringUtils.isBlank(id)) {
            Container container = (Container) ViewLifecycle.getPhase().getElement();
            id = container.getId() + "_layout";
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performApplyModel(Object model, LifecycleElement component) {
        checkMutable(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void performFinalize(Object model, LifecycleElement component) {
        checkMutable(false);

        // put together all css class names for this component, in order
        List<String> finalCssClasses = new ArrayList<String>();
        
        View view = ViewLifecycle.getView();

        if (this.libraryCssClasses != null && view.isUseLibraryCssClasses()) {
            finalCssClasses.addAll(libraryCssClasses);
        }

        if (this.cssClasses != null) {
            finalCssClasses.addAll(cssClasses);
        }

        if (this.additionalCssClasses != null) {
            finalCssClasses.addAll(additionalCssClasses);
        }

        cssClasses = finalCssClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initializePendingTasks(ViewLifecyclePhase phase, Queue<ViewLifecycleTask<?>> pendingTasks) {
    }

    /**
     * Default Impl
     *
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getSupportedContainer()
     */
    @Override
    public Class<? extends Container> getSupportedContainer() {
        return Container.class;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getComponentsForLifecycle()
     */
    @Override
    public final List<Component> getComponentsForLifecycle() {
        return new ArrayList<Component>();
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getComponentPrototypes()
     */
    @Override
    public final List<Component> getComponentPrototypes() {
        List<Component> components = new ArrayList<Component>();

        return components;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getId()
     */
    @Override
    @BeanTagAttribute(name = "id")
    public String getId() {
        return this.id;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setId(java.lang.String)
     */
    @Override
    public void setId(String id) {
        checkMutable(true);
        this.id = id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getViewPath() {
        return this.viewPath;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setViewPath(String viewPath) {
        checkMutable(true);
        this.viewPath = viewPath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getPhasePathMapping() {
        return phasePathMapping;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPhasePathMapping(Map<String, String> phasePathMapping) {
        this.phasePathMapping = phasePathMapping;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getTemplate()
     */
    @Override
    @BeanTagAttribute(name = "template")
    public String getTemplate() {
        return this.template;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setTemplate(java.lang.String)
     */
    @Override
    public void setTemplate(String template) {
        checkMutable(true);
        this.template = template;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getTemplateName()
     */
    @BeanTagAttribute(name = "tempateName")
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setTemplateName(java.lang.String)
     */
    public void setTemplateName(String templateName) {
        checkMutable(true);
        this.templateName = templateName;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getStyle()
     */
    @Override
    @BeanTagAttribute(name = "Style")
    public String getStyle() {
        return this.style;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setStyle(java.lang.String)
     */
    @Override
    public void setStyle(String style) {
        checkMutable(true);
        this.style = style;
    }

    /**
     * Additional css classes that come before css classes listed in the cssClasses property
     * 
     * <p>
     * These are used by the framework for styling with a library (for example, bootstrap), and
     * should normally not be overridden.
     * </p>
     * 
     * @return the library cssClasses
     */
    public List<String> getLibraryCssClasses() {
        if (libraryCssClasses == Collections.EMPTY_LIST && isMutable(true)) {
            libraryCssClasses = new LifecycleAwareList<String>(this);
        }
        
        return libraryCssClasses;
    }

    /**
     * Set the libraryCssClasses
     * 
     * @param libraryCssClasses
     */
    public void setLibraryCssClasses(List<String> libraryCssClasses) {
        checkMutable(true);

        if (libraryCssClasses == null) {
            this.libraryCssClasses = Collections.emptyList();
        } else {
            this.libraryCssClasses = new LifecycleAwareList<String>(this, libraryCssClasses);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getCssClasses()
     */
    @BeanTagAttribute(name = "cssClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getCssClasses() {
        if (cssClasses == Collections.EMPTY_LIST && isMutable(true)) {
            cssClasses = new LifecycleAwareList<String>(this);
        }
        
        return cssClasses;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setCssClasses(java.util.List)
     */
    public void setCssClasses(List<String> cssClasses) {
        checkMutable(true);
        if (cssClasses == null) {
            this.cssClasses = Collections.emptyList();
        } else {
            this.cssClasses = new LifecycleAwareList<String>(this, cssClasses);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getAdditionalCssClasses()
     */
    @BeanTagAttribute(name = "additionalCssClasses", type = BeanTagAttribute.AttributeType.LISTVALUE)
    public List<String> getAdditionalCssClasses() {
        if (additionalCssClasses == Collections.EMPTY_LIST && isMutable(true)) {
            additionalCssClasses = new LifecycleAwareList<String>(this);
        }
        
        return additionalCssClasses;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setAdditionalCssClasses(java.util.List)
     */
    public void setAdditionalCssClasses(List<String> additionalCssClasses) {
        checkMutable(true);
        if (additionalCssClasses == null) {
            this.additionalCssClasses = Collections.emptyList();
        } else {
            this.additionalCssClasses = new LifecycleAwareList<String>(this, additionalCssClasses);
        }
    }

    /**
     * Builds the HTML class attribute string by combining the styleClasses list
     * with a space delimiter
     *
     * @return class attribute string
     */
    public String getStyleClassesAsString() {
        if (cssClasses != null) {
            return StringUtils.join(cssClasses, " ");
        }

        return "";
    }

    /**
     * Sets the styleClasses list from the given string that has the classes
     * delimited by space. This is a convenience for configuration. If a child
     * bean needs to inherit the classes from the parent, it should configure as
     * a list and use merge="true"
     *
     * @param styleClasses
     */
    public void setStyleClasses(String styleClasses) {
        checkMutable(true);
        String[] classes = StringUtils.split(styleClasses);
        this.cssClasses = Arrays.asList(classes);
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#addStyleClass(java.lang.String)
     */
    @Override
    public void addStyleClass(String styleClass) {
        checkMutable(false);
        if (cssClasses == null || cssClasses.isEmpty()) {
            cssClasses = new ArrayList<String>();
        }
        
        if (!cssClasses.contains(styleClass)) {
            cssClasses.add(styleClass);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#appendToStyle(java.lang.String)
     */
    @Override
    public void appendToStyle(String styleRules) {
        checkMutable(false);
        if (style == null) {
            style = "";
        }
        style = style + styleRules;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getContext()
     */
    @Override
    @BeanTagAttribute(name = "context", type = BeanTagAttribute.AttributeType.MAPBEAN)
    public Map<String, Object> getContext() {
        if (context == Collections.EMPTY_MAP && isMutable(true)) {
            context = new LifecycleAwareMap<String, Object>(this);
        }
        
        return context;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setContext(java.util.Map)
     */
    @Override
    public void setContext(Map<String, Object> context) {
        checkMutable(true);

        if (context == null) {
            this.context = Collections.emptyMap();
        } else {
            this.context = new LifecycleAwareMap<String, Object>(this, context);
        }
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#pushObjectToContext(java.lang.String,
     *      java.lang.Object)
     */
    @Override
    public void pushObjectToContext(String objectName, Object object) {
        checkMutable(false);
        if (context == Collections.EMPTY_MAP && isMutable(true)) {
            context = new LifecycleAwareMap<String, Object>(this);
        }

        context.put(objectName, object);
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#pushAllToContext(java.util.Map)
     */
    @Override
    public void pushAllToContext(Map<String, Object> sourceContext) {
        checkMutable(false);
        if (sourceContext == null || sourceContext.isEmpty()) {
            return;
        }
        
        if (context == Collections.EMPTY_MAP && isMutable(true)) {
            context = new LifecycleAwareMap<String, Object>(this);
        }

        this.context.putAll(sourceContext);
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#getPropertyReplacers()
     */
    @Override
    @BeanTagAttribute(name = "propertyReplacers", type = BeanTagAttribute.AttributeType.LISTBEAN)
    public List<PropertyReplacer> getPropertyReplacers() {
        return this.propertyReplacers;
    }

    /**
     * @see org.kuali.rice.krad.uif.layout.LayoutManager#setPropertyReplacers(java.util.List)
     */
    @Override
    public void setPropertyReplacers(List<PropertyReplacer> propertyReplacers) {
        checkMutable(true);
        this.propertyReplacers = propertyReplacers;
    }

    @Override
    public LayoutManagerBase clone() throws CloneNotSupportedException {
        LayoutManagerBase copy = (LayoutManagerBase) super.clone();

        // Copy initialized status, but reset to created for others.
        // This allows prototypes to bypass repeating the initialized phase.
        if (UifConstants.ViewStatus.INITIALIZED.equals(viewStatus)) {
            copy.viewStatus = UifConstants.ViewStatus.INITIALIZED;
        } else {
            copy.viewStatus = UifConstants.ViewStatus.CREATED;
        }

        return copy;
    }
    
    /**
     * Set view status to {@link org.kuali.rice.krad.uif.UifConstants.ViewStatus#CACHED} to prevent modification.
     * 
     * @see Copyable#preventModification()
     */
    @Override
    public void preventModification() {
        if (!UifConstants.ViewStatus.CREATED.equals(viewStatus)
                && !UifConstants.ViewStatus.CACHED.equals(viewStatus)) {
            ViewLifecycle.reportIllegalState("View status is " + viewStatus + " prior to caching "
                    + getClass().getName() + " " + getId() + ", expected C or X");
        }

        viewStatus = UifConstants.ViewStatus.CACHED;
    }

    /**
     * Indicates whether the component has been initialized.
     * 
     * @return True if the component has been initialized, false if not.
     */
    public boolean isInitialized() {
        return StringUtils.equals(viewStatus, ViewStatus.INITIALIZED) || isModelApplied();
    }

    /**
     * Indicates whether the component has been updated from the model.
     * 
     * @return True if the component has been updated, false if not.
     */
    public boolean isModelApplied() {
        return StringUtils.equals(viewStatus, ViewStatus.MODEL_APPLIED) || isFinal();
    }

    /**
     * Indicates whether the component has been updated from the model and final updates made.
     * 
     * @return True if the component has been updated, false if not.
     */
    public boolean isFinal() {
        return StringUtils.equals(viewStatus, ViewStatus.FINAL);
    }
    
    /**
     * @see org.kuali.rice.krad.datadictionary.DictionaryBeanBase#copyProperties(Object)
     */
    @Override
    protected <T> void copyProperties(T layoutManager) {
        super.copyProperties(layoutManager);

        LayoutManagerBase layoutManagerBaseCopy = (LayoutManagerBase) layoutManager;

        // Copy initialized status, but reset to created for others.
        // This allows prototypes to bypass repeating the initialized phase.
        if (UifConstants.ViewStatus.INITIALIZED.equals(viewStatus)) {
            layoutManagerBaseCopy.viewStatus = UifConstants.ViewStatus.INITIALIZED;
        } else {
            layoutManagerBaseCopy.viewStatus = UifConstants.ViewStatus.CREATED;
        }

        layoutManagerBaseCopy.setId(this.id);

        if (this.phasePathMapping != null) {
            layoutManagerBaseCopy.setPhasePathMapping(new HashMap<String, String>(this.phasePathMapping));
        }

        layoutManagerBaseCopy.setTemplate(this.template);
        layoutManagerBaseCopy.setTemplateName(this.templateName);
        layoutManagerBaseCopy.setStyle(this.style);

        if (libraryCssClasses != null) {
            layoutManagerBaseCopy.setLibraryCssClasses(new ArrayList<String>(libraryCssClasses));
        }

        if (cssClasses != null) {
            layoutManagerBaseCopy.setCssClasses(new ArrayList<String>(cssClasses));
        }

        if (additionalCssClasses != null) {
            layoutManagerBaseCopy.setAdditionalCssClasses(new ArrayList<String>(additionalCssClasses));
        }

        if (getPropertyReplacers() != null) {
            List<PropertyReplacer> propertyReplacersCopy = new ArrayList<PropertyReplacer>();
            for (PropertyReplacer propertyReplacer : propertyReplacers) {
                propertyReplacersCopy.add((PropertyReplacer) propertyReplacer.copy());
            }

            layoutManagerBaseCopy.setPropertyReplacers(propertyReplacersCopy);
        }
    }
}
