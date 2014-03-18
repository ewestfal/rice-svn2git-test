/*
 * Copyright 2007-2009 The Kuali Foundation
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
package org.kuali.rice.kim.bo.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.kuali.rice.kim.bo.Role;
import org.kuali.rice.kim.bo.role.dto.KimRoleInfo;
import org.kuali.rice.kim.bo.role.impl.RoleMemberImpl;
import org.kuali.rice.kim.bo.types.dto.KimTypeInfo;
import org.kuali.rice.kim.service.KIMServiceLocator;
import org.kuali.rice.kim.service.KimTypeInfoService;
import org.kuali.rice.kim.util.KimConstants;
import org.kuali.rice.kns.bo.PersistableBusinessObjectBase;
import org.kuali.rice.kns.util.TypedArrayList;

/**
 * This is a description of what this class does - shyu don't forget to fill this in. 
 * 
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
@Entity
@Table(name="KRIM_ROLE_T")
public class RoleImpl extends PersistableBusinessObjectBase implements Role {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="ROLE_ID")
	protected String roleId;
	@Column(name="ROLE_NM")
	protected String roleName;
	@Column(name="DESC_TXT",length=4000)
	protected String roleDescription;
	@Column(name="ACTV_IND")
	protected boolean active;
	@Column(name="KIM_TYP_ID")
	protected String kimTypeId;
	@Column(name="NMSPC_CD")
	protected String namespaceCode;

	@OneToMany(targetEntity=RoleMemberImpl.class,cascade={CascadeType.ALL},fetch=FetchType.LAZY)
	@JoinColumn(name="ROLE_ID", insertable=false, updatable=false)
	protected List<RoleMemberImpl> members = new TypedArrayList(RoleMemberImpl.class);

	protected String principalName;
	protected String groupNamespaceCode;
	protected String groupName;
	protected String permNamespaceCode;
	protected String permName;
	protected String permTmplNamespaceCode;
	protected String permTmplName;
	protected String respNamespaceCode;
	protected String respName;
	protected String respTmplNamespaceCode;
	protected String respTmplName;
	
	/**
	 * This overridden method ...
	 * 
	 * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringMapper()
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected LinkedHashMap toStringMapper() {
		LinkedHashMap m = new LinkedHashMap();
		m.put( "roleId", roleId );
		m.put( "namespaceCode", namespaceCode );
		m.put( "roleName", roleName );
		return m;
	}

	public String getRoleId() {
		return this.roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getKimTypeId() {
		return this.kimTypeId;
	}

	public void setKimTypeId(String kimTypeId) {
		this.kimTypeId = kimTypeId;
	}

	public String getNamespaceCode() {
		return this.namespaceCode;
	}

	public void setNamespaceCode(String namespaceCode) {
		this.namespaceCode = namespaceCode;
	}

	public String getPrincipalName() {
		return this.principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getGroupNamespaceCode() {
		return this.groupNamespaceCode;
	}

	public void setGroupNamespaceCode(String groupNamespaceCode) {
		this.groupNamespaceCode = groupNamespaceCode;
	}

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPermNamespaceCode() {
		return this.permNamespaceCode;
	}

	public void setPermNamespaceCode(String permNamespaceCode) {
		this.permNamespaceCode = permNamespaceCode;
	}

	public String getPermName() {
		return this.permName;
	}

	public void setPermName(String permName) {
		this.permName = permName;
	}

	public String getRespNamespaceCode() {
		return this.respNamespaceCode;
	}

	public void setRespNamespaceCode(String respNamespaceCode) {
		this.respNamespaceCode = respNamespaceCode;
	}

	public String getRespName() {
		return this.respName;
	}

	public void setRespName(String respName) {
		this.respName = respName;
	}

	public String getPermTmplNamespaceCode() {
		return this.permTmplNamespaceCode;
	}

	public void setPermTmplNamespaceCode(String permTmplNamespaceCode) {
		this.permTmplNamespaceCode = permTmplNamespaceCode;
	}

	public String getPermTmplName() {
		return this.permTmplName;
	}

	public void setPermTmplName(String permTmplName) {
		this.permTmplName = permTmplName;
	}

	public String getRespTmplNamespaceCode() {
		return this.respTmplNamespaceCode;
	}

	public void setRespTmplNamespaceCode(String respTmplNamespaceCode) {
		this.respTmplNamespaceCode = respTmplNamespaceCode;
	}

	public String getRespTmplName() {
		return this.respTmplName;
	}

	public void setRespTmplName(String respTmplName) {
		this.respTmplName = respTmplName;
	}

	/**
	 * @return the roleDescription
	 */
	public String getRoleDescription() {
		return this.roleDescription;
	}

	/**
	 * @param roleDescription the roleDescription to set
	 */
	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	protected List<String> getMembersOfType( String memberTypeCode ) {
		List<String> roleMembers = new ArrayList<String>();
		for ( RoleMemberImpl member : getMembers() ) {
			if ( member.getMemberTypeCode().equals ( memberTypeCode )
					&& member.isActive() ) {
				roleMembers.add( member.getMemberId() );
			}
		}
		return roleMembers;
	}


	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if ( !(object instanceof Role) ) {
			return false;
		}
		Role rhs = (Role)object;
		return new EqualsBuilder().append( this.roleId, rhs.getRoleId() ).isEquals();
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder( -460627871, 746615189 ).append( this.roleId ).toHashCode();
	}

	public KimTypeInfo getKimRoleType() {
		return getTypeInfoService().getKimType(kimTypeId);
	}
	private transient static KimTypeInfoService kimTypeInfoService;
	protected KimTypeInfoService getTypeInfoService() {
		if(kimTypeInfoService == null){
			kimTypeInfoService = KIMServiceLocator.getTypeInfoService();
		}
		return kimTypeInfoService;
	}

	public KimRoleInfo toSimpleInfo() {
		KimRoleInfo dto = new KimRoleInfo();
		
		dto.setRoleId( getRoleId() );
		dto.setRoleName( getRoleName() );
		dto.setNamespaceCode( getNamespaceCode() );
		dto.setRoleDescription( getRoleDescription() );
		dto.setKimTypeId( getKimTypeId() );
		dto.setActive( isActive() );
		
		return dto;
	}

	public List<RoleMemberImpl> getMembers() {
		return this.members;
	}

	public void setMembers(List<RoleMemberImpl> members) {
		this.members = members;
	}
	
	/**
	 * @see org.kuali.rice.kns.bo.BusinessObjectBase#toStringBuilder(java.util.LinkedHashMap)
	 */
	@SuppressWarnings("unchecked")
	@Override
    public String toStringBuilder(LinkedHashMap mapper) {
        if(getKimRoleType() != null){
        	return getKimRoleType().getName()+KimConstants.KimUIConstants.NAME_VALUE_SEPARATOR+
        				getNamespaceCode()+KimConstants.KimUIConstants.NAME_VALUE_SEPARATOR+
        				getRoleName()+KimConstants.KimUIConstants.COMMA_SEPARATOR;
        }
        else {
            return super.toStringBuilder(mapper);
        }
    }

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
}