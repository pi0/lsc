/*
 ****************************************************************************
 * Ldap Synchronization Connector provides tools to synchronize
 * electronic identities from a list of data sources including
 * any database with a JDBC connector, another LDAP directory,
 * flat files...
 *
 *                  ==LICENSE NOTICE==
 * 
 * Copyright (c) 2008, LSC Project 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:

 *    * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of the LSC Project nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *                  ==LICENSE NOTICE==
 *
 *               (c) 2008 - 2009 LSC Project
 *         Sebastien Bahloul <seb@lsc-project.org>
 *         Thomas Chemineau <thomas@lsc-project.org>
 *         Jonathan Clarke <jon@lsc-project.org>
 *         Remy-Christophe Schermesser <rcs@lsc-project.org>
 ****************************************************************************
 */
package org.lsc.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.naming.directory.BasicAttribute;

import org.junit.Test;

import static org.junit.Assert.*;

import org.lsc.beans.syncoptions.ForceSyncOptions;
import org.lsc.beans.syncoptions.ISyncOptions;
import org.lsc.beans.syncoptions.ISyncOptions.STATUS_TYPE;
import org.lsc.jndi.JndiModificationType;
import org.lsc.jndi.JndiModifications;
import org.lsc.utils.SetUtils;

/**
 * @author Jonathan Clarke &lt;jonathan@phillipoux.net&gt;
 *
 */
public class BeanComparatorTest {

	/**
	 * Test method for {@link org.lsc.beans.BeanComparator#calculateModificationType(ISyncOptions, IBean, IBean, Object)}.
	 */
	@Test
	public void testCalculateModificationType() throws CloneNotSupportedException {
		dummySyncOptions syncOptions = new dummySyncOptions();
		IBean srcBean = new SimpleBean();
		IBean dstBean = new SimpleBean();


		// test null and null --> null
		assertNull(BeanComparator.calculateModificationType(syncOptions, null, null, null));

		// test not null and null --> add
		assertEquals(JndiModificationType.ADD_ENTRY, BeanComparator.calculateModificationType(syncOptions, srcBean, null, null));

		// test null and not null --> delete
		assertEquals(JndiModificationType.DELETE_ENTRY, BeanComparator.calculateModificationType(syncOptions, null, dstBean, null));

		// test both not null, and syncoptions to make DNs identical --> modify
		syncOptions.setDn("\"destination DN\"");
		dstBean.setDistinguishedName("destination DN");
		assertEquals(JndiModificationType.MODIFY_ENTRY, BeanComparator.calculateModificationType(syncOptions, srcBean, dstBean, null));

		// test both not null, with different DNs and no DN in syncoptions --> modrdn
		syncOptions.setDn(null);
		srcBean.setDistinguishedName("source DN");
		dstBean.setDistinguishedName("destination DN");
		assertEquals(JndiModificationType.MODRDN_ENTRY, BeanComparator.calculateModificationType(syncOptions, srcBean, dstBean, null));
	}

	/**
	 * This test ensures that a source bean containing fields with only
	 * empty string values is not output as a modification to be applied
	 * to the destination.
	 *
	 * If this is not the case, LDAP operations follow for LDIF like this:
	 * modificationType: add
	 * add: sn
	 * sn:
	 *
	 * With an invalid syntax error.
	 */
	@Test
	public void testCalculateModificationsWithEmptyFieldsAdd() throws NamingException, CloneNotSupportedException {
		ISyncOptions syncOptions = new ForceSyncOptions();
		IBean srcBean, destBean;
		Object customLibrary = null;
		boolean condition = true;

		// test add
		srcBean = new SimpleBean();
		srcBean.setDistinguishedName("something");
		srcBean.setAttribute(new BasicAttribute("sn", ""));
		srcBean.setAttribute(new BasicAttribute("cn", "real cn"));
		destBean = null;

		JndiModifications jm = BeanComparator.calculateModifications(syncOptions, srcBean, destBean, customLibrary, condition);

		assertEquals("something", jm.getDistinguishName());
		assertEquals(1, jm.getModificationItems().size());
		assertEquals("cn", jm.getModificationItems().get(0).getAttribute().getID());
		assertEquals("real cn", jm.getModificationItems().get(0).getAttribute().get());
	}

	@Test
	public void testCalculateModificationsWithEmptyFieldsModify() throws NamingException, CloneNotSupportedException {
		ISyncOptions syncOptions = new ForceSyncOptions();
		IBean srcBean, destBean;
		Object customLibrary = null;
		boolean condition = true;

		// test mod
		srcBean = new SimpleBean();
		srcBean.setDistinguishedName("something");
		srcBean.setAttribute(new BasicAttribute("sn", ""));
		srcBean.setAttribute(new BasicAttribute("cn", "real cn"));

		destBean = new SimpleBean();
		destBean.setDistinguishedName("something");
		destBean.setAttribute(new BasicAttribute("cn", "old cn"));

		JndiModifications jm = BeanComparator.calculateModifications(syncOptions, srcBean, destBean, customLibrary, condition);

		assertEquals("something", jm.getDistinguishName());
		assertEquals(1, jm.getModificationItems().size());
		assertEquals("cn", jm.getModificationItems().get(0).getAttribute().getID());
		assertEquals("real cn", jm.getModificationItems().get(0).getAttribute().get());

	}

	/**
	 * Test method for {@link org.lsc.beans.BeanComparator#getValuesToSet(String, Set, ISyncOptions, Map, JndiModificationType)}.
	 */
	@Test
	public void testGetValuesToSet() throws NamingException {

		// Set up objects needed to test
		String attrName = "cn";

		Set<Object> srcAttrValues = null;
		ISyncOptions syncOptions = null;
		Map<String, Object> javaScriptObjects = null;

		Map<String, List<String>> createValuesMap = new HashMap<String, List<String>>();
		Map<String, List<String>> defaultValuesMap = new HashMap<String, List<String>>();
		Map<String, List<String>> forceValuesMap = new HashMap<String, List<String>>();
		Map<String, STATUS_TYPE> statusMap = null;

		List<String> jsValues = new ArrayList<String>();
		jsValues.add("\"JavaScript \" + (true ? \"has\" : \"hasn't\") + \" parsed this value (0)\"");
		jsValues.add("\"JavaScript \" + (true ? \"has\" : \"hasn't\") + \" parsed this value (1)\"");

		List<String> jsCreateValues = new ArrayList<String>();
		jsCreateValues.add("\"Created by JavaScript \" + (true ? \"successfully\" : \"or not\") + \" (0)\"");
		jsCreateValues.add("\"Created by JavaScript \" + (true ? \"successfully\" : \"or not\") + \" (1)\"");


		createValuesMap.put("cn", jsCreateValues);
		defaultValuesMap.put("cn", jsValues);
		forceValuesMap.put("cn", jsValues);

		Set<Object> res = null;


		// First test: no default values, no force values, no source values (empty list)
		// Should return an empty List
		srcAttrValues = new HashSet<Object>();
		syncOptions = new dummySyncOptions(null, null, null, null);
		javaScriptObjects = new HashMap<String, Object>();

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(0, res.size());


		// First test again: no default values, no force values, no source values (null list)
		// Should return an empty List
		srcAttrValues = null;
		syncOptions = new dummySyncOptions(null, null, null, null);
		javaScriptObjects = new HashMap<String, Object>();

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(0, res.size());


		// Second test: just source values.
		// Should return the source values, unchanged
		srcAttrValues = new HashSet<Object>();
		srcAttrValues.add("Megan Fox");
		srcAttrValues.add("Lucy Liu");
		syncOptions = new dummySyncOptions(createValuesMap, null, null, null);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(srcAttrValues.size(), res.size());
		assertTrue(SetUtils.doSetsMatch(srcAttrValues, res));


		// Third test: source values to be replaced with force values
		// Should return just the force values
		srcAttrValues = new HashSet<Object>();
		srcAttrValues.add("Megan Fox");
		srcAttrValues.add("Lucy Liu");
		syncOptions = new dummySyncOptions(null, null, forceValuesMap, null);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(jsValues.size(), res.size());
		for (int i = 0; i < jsValues.size(); i++) {
			assertTrue(res.contains("JavaScript has parsed this value (" + i + ")"));
		}


		// Fourth test: no source values, no force values, just default values
		// Should return just the default values
		srcAttrValues = new HashSet<Object>();
		syncOptions = new dummySyncOptions(null, defaultValuesMap, null, null);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(jsValues.size(), res.size());
		for (int i = 0; i < jsValues.size(); i++) {
			assertTrue(res.contains("JavaScript has parsed this value (" + i + ")"));
		}


		// 5th test: source values, and default values, attribute status "Force"
		// Should return just source values
		srcAttrValues = new HashSet<Object>();
		statusMap = new HashMap<String, STATUS_TYPE>();
		statusMap.put(attrName, STATUS_TYPE.FORCE);
		syncOptions = new dummySyncOptions(null, defaultValuesMap, null, statusMap);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(jsValues.size(), res.size());
		for (int i = 0; i < jsValues.size(); i++) {
			assertTrue(res.contains("JavaScript has parsed this value (" + i + ")"));
		}


		// 6th test: source values, and default values, attribute status "Merge"
		// Should return source values AND default values
		srcAttrValues = new HashSet<Object>();
		srcAttrValues.add("Megan Fox");
		srcAttrValues.add("Lucy Liu");
		statusMap = new HashMap<String, STATUS_TYPE>();
		statusMap.put(attrName, STATUS_TYPE.MERGE);
		syncOptions = new dummySyncOptions(null, defaultValuesMap, null, statusMap);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNotNull(res);
		assertEquals(jsValues.size() + srcAttrValues.size(), res.size());
		Set<Object> expectedValues = new HashSet<Object>(jsValues.size() + srcAttrValues.size());
		for (Object srcAttrValue : srcAttrValues) {
			expectedValues.add(srcAttrValue);
		}
		for (int i = 0; i < jsValues.size(); i++) {
			expectedValues.add("JavaScript has parsed this value (" + i + ")");
		}
		assertTrue(SetUtils.doSetsMatch(res, expectedValues));


		// 7th test: source values, and create values, attribute status "Merge", creating new entry
		// Should return source values AND create values but no default values
		srcAttrValues = new HashSet<Object>();
		srcAttrValues.add("Megan Fox");
		srcAttrValues.add("Lucy Liu");
		statusMap = new HashMap<String, STATUS_TYPE>();
		statusMap.put(attrName, STATUS_TYPE.MERGE);
		syncOptions = new dummySyncOptions(createValuesMap, defaultValuesMap, null, statusMap);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.ADD_ENTRY);

		assertNotNull(res);
		assertEquals(jsCreateValues.size() + srcAttrValues.size(), res.size());
		expectedValues = new HashSet<Object>(jsCreateValues.size() + srcAttrValues.size());
		for (Object srcAttrValue : srcAttrValues) {
			expectedValues.add(srcAttrValue);
		}
		for (int i = 0; i < jsCreateValues.size(); i++) {
			expectedValues.add("Created by JavaScript successfully (" + i + ")");
		}
		assertTrue(SetUtils.doSetsMatch(res, expectedValues));


		// test that attributes configured for create values only
		// (no force_values, default_values or source values)
		// are ignored if this is not an Add operation
		srcAttrValues = new HashSet<Object>();
		syncOptions = new dummySyncOptions(createValuesMap, null, null, null);

		res = BeanComparator.getValuesToSet(attrName, srcAttrValues, syncOptions, javaScriptObjects, JndiModificationType.MODIFY_ENTRY);

		assertNull(res);
	}

	public class dummySyncOptions implements ISyncOptions {

		Map<String, List<String>> createValuesMap;
		Map<String, List<String>> defaultValuesMap;
		Map<String, List<String>> forceValuesMap;
		Map<String, STATUS_TYPE> statusMap;
		String dn;

		public dummySyncOptions(Map<String, List<String>> createValuesMap,
						Map<String, List<String>> defaultValuesMap,
						Map<String, List<String>> forceValuesMap,
						Map<String, STATUS_TYPE> statusMap) {
			if (createValuesMap != null) {
				this.createValuesMap = createValuesMap;
			} else {
				this.createValuesMap = new HashMap<String, List<String>>();
			}

			if (defaultValuesMap != null) {
				this.defaultValuesMap = defaultValuesMap;
			} else {
				this.defaultValuesMap = new HashMap<String, List<String>>();
			}

			if (forceValuesMap != null) {
				this.forceValuesMap = forceValuesMap;
			} else {
				this.forceValuesMap = new HashMap<String, List<String>>();
			}

			if (statusMap != null) {
				this.statusMap = statusMap;
			} else {
				this.statusMap = new HashMap<String, STATUS_TYPE>();
			}

		}

		public dummySyncOptions() {
			// TODO Auto-generated constructor stub
		}

		public String getCondition(JndiModificationType operation) {
			// TODO Auto-generated method stub
			return null;
		}

		public Set<String> getCreateAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getCreateCondition() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<String> getCreateValues(String id, String attributeName) {
			return createValuesMap.get(attributeName);
		}

		public Set<String> getDefaultValuedAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<String> getDefaultValues(String id, String attributeName) {
			return defaultValuesMap.get(attributeName);
		}

		public String getDeleteCondition() {
			// TODO Auto-generated method stub
			return null;
		}

		public void setDn(String dn) {
			this.dn = dn;
		}

		public String getDn() {
			return dn;
		}

		public Set<String> getForceValuedAttributeNames() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<String> getForceValues(String id, String attributeName) {
			return forceValuesMap.get(attributeName);
		}

		public String getModrdnCondition() {
			// TODO Auto-generated method stub
			return null;
		}

		public STATUS_TYPE getStatus(String id, String attributeName) {
			return statusMap.get(attributeName);
		}

		public String getTaskName() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getUpdateCondition() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<String> getWriteAttributes() {
			// TODO Auto-generated method stub
			return null;
		}

		public void initialize(String taskname) {
			// TODO Auto-generated method stub
		}
	}
}
