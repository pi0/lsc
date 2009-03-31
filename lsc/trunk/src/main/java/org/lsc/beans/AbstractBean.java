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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.SearchResult;

import org.apache.log4j.Logger;
import org.lsc.Configuration;
import org.lsc.objects.top;
import org.lsc.utils.DateUtils;


/**
 * Abstract bean used to centralize methods across all beans
 *
 * The main purpose of this class is to provide the mapper method which allow
 * the bean to be mapped from the original object.
 *
 * The bean is also able to map a bean from SearchResult LDAP object.
 *
 * @author Sebastien Bahloul &lt;seb@lsc-project.org&gt;
 */
public abstract class AbstractBean implements IBean {

    /**
     * For eclipse
     */
    private static final long serialVersionUID = -3088695839587594711L;

    /** Initialize the static local methods table contents. */
    static {
        localMethods = new HashMap<String, Method>();
    }

    /** The local LOG4J logger. */
    private static final Logger LOGGER = Logger.getLogger(AbstractBean.class);

    /** The accessor getter prefix. */
    public static final String GET_ACCESSOR_PREFIX = "get";

    /** The mapper method prefix. */
    public static final String MAP_FUNCTION_PREFIX = "map";

    /** The accessor setter prefix. */
    public static final String SET_ACCESSOR_PREFIX = "set";

    /** The list of local methods. */
    private static Map<String, Method> localMethods;

    /** The attributes map. */
    private Map<String, Attribute> attrs;

    /** The distinguish name. */
    private String distinguishName;

    /**
     * The default constructor.
     */
    public AbstractBean() {
        attrs = new HashMap<String, Attribute>();
        localMethods = new HashMap<String, Method>();
    }

    /**
     * Load all the object values into the bean by invoking either the
     * object bean map function or the default mapper.
     * TODO: Check if loading local methods each time can be improved
     *
     * @param cl
     *                the local class which may be used to check
     *                Introspection
     * @param bean
     *                the object to fill
     * @param o
     *                object from which the values will be taken
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void mapper(final Class<?> cl, IBean bean, final Object o)
    throws IllegalAccessException, InvocationTargetException {
        localMethods = new HashMap<String, Method>();
        loadLocalMethods(cl, localMethods, MAP_FUNCTION_PREFIX);

        Method[] methods = o.getClass().getMethods();

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().startsWith(GET_ACCESSOR_PREFIX) &&
                    (methods[i].getName().substring(GET_ACCESSOR_PREFIX.length())
                            .compareToIgnoreCase("dn") != 0)) {
                /* Getting parameter name */
                String paramName = methods[i].getName().substring(GET_ACCESSOR_PREFIX.length());
                paramName = paramName.substring(0, 1).toLowerCase() + paramName.substring(1);

                Class<?> returnType = methods[i].getReturnType();

                /* Getting local method */
                Method localMethod = localMethods.get(paramName);

                if (localMethod != null) {
                    Object o2 = methods[i].invoke(o, new Object[] {  });
                    Object[] params = new Object[] { o, (IBean) bean, o2 };
                    Class<?>[] paramsType = localMethod.getParameterTypes();

                    try {
                        localMethod.invoke(bean, params);
                    } catch (IllegalArgumentException iae) {
                        if (o2 != null) {
                            LOGGER.error(
                                    "Unable to invoke the method because values class differs : bean wait for " +
                                    paramsType[2].getName() +
                                    " where as object can provide " +
                                    o2.getClass().getName() + " in method " +
                                    localMethod.toString() + " (" + iae + ")", iae);
                        } else {
                            LOGGER.error(
                                    "Unable to invoke the method because values class differs : bean wait for " +
                                    paramsType[2].getName() +
                                    " where as object is null " + " in method " +
                                    localMethod.toString() + " (" + iae + ")", iae);
                        }
                    }
                } else {
                    if (returnType == String.class) {
                        mapString(bean, paramName, (String) methods[i].invoke(o, new Object[] { }));
                    } else if (returnType == Integer.class) {
                        mapString(bean, paramName, ((Integer) methods[i].invoke(o, new Object[] { })).toString());
                    } else if (returnType == Date.class) {
                        mapString(bean, paramName, DateUtils.format(((Date) methods[i].invoke(o, new Object[] { }))));
                    } else if (returnType == Boolean.class) {
                        boolean bValue = ((Boolean) methods[i].invoke(o, new Object[] { })).booleanValue();

                        if (bValue) {
                            mapString(bean, paramName, "TRUE");
                        } else {
                            mapString(bean, paramName, "FALSE");
                        }
                    } else if (List.class.isAssignableFrom(returnType)) {
                        mapList(bean, paramName, (List<?>) methods[i].invoke(o, new Object[] {  }));
                    } else if (Map.class.isAssignableFrom(returnType)) {
                        mapMap(bean, paramName, (Map<?, ?>) methods[i].invoke(o, new Object[] {  }));
                    }
                }
            }
        }
    }

    /**
     * Load the prefixed methods of the class into the map
     *
     * @param cl the class to introspect
     * @param lMs the method map
     * @param prefix the methods prefix to look for
     */
    public static void loadLocalMethods(final Class<?> cl,
            final Map<String, Method> lMs, final String prefix) {
        Method[] methods = cl.getMethods();

        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().startsWith(prefix)) {
                String paramName = methods[i].getName().substring(prefix.length());
                paramName = paramName.substring(0, 1).toLowerCase() + paramName.substring(1);
                lMs.put(paramName, methods[i]);
            }
        }
    }

    /**
     * Map a list into the bean.
     * This method is used to include String filtering
     * @param bean the bean
     * @param paramName the parameter name
     * @param values the values list
     */
    protected static void mapList(IBean bean, String paramName, List<?> values) {
        Attribute attr = new BasicAttribute(paramName);

        if (values != null) {
            Iterator<?> iter = values.iterator();

            while (iter.hasNext()) {
                String value = ((String) iter.next()).trim();

                if (value.length() > 0) {
                    attr.add(value);
                }
            }
        }

        bean.setAttribute(attr);
    }

    /**
     * Map a structure map into the bean
     *
     * This method is used to filter qualified string (in the form
     * \\{[A-Za-z0-9]+\\}.+
     *
     * @param bean
     *                the bean
     * @param paramName
     *                the parameter name
     * @param values
     *                the values map
     */
    protected static void mapMap(IBean bean, String paramName, Map<?, ?> values) {
        Attribute attr = new BasicAttribute(paramName);

        if (values != null) {
            Iterator<?> iter = values.keySet().iterator();

            while (iter.hasNext()) {
                String key = (String) iter.next();
                List<?> codesValues = (List<?>) values.get(key);
                Iterator<?> codesValuesIter = codesValues.iterator();

                while (codesValuesIter.hasNext()) {
                    // TODO: check consistency
                    attr.add("{" + key.trim() + "}" +
                            ((String) codesValuesIter.next()).trim());
                }
            }
        }

        bean.setAttribute(attr);
    }

    /**
     * Map a list into the bean
     *
     * This method is used to include String filtering
     *
     * @param bean
     *                the bean to set
     * @param paramName
     *                the attribute name
     * @param value
     *                the string value
     */
    protected static void mapString(IBean bean, String paramName, String value) {
        Attribute attr = new BasicAttribute(paramName);

        if ((value != null) && (value.trim().length() > 0)) {
            attr.add(value.trim());
        }

        bean.setAttribute(attr);
    }

    /**
     * Method implemented to bypass the default distinguish name mapping.
     *
     * @param t
     *                the original attribute
     * @param destBean
     *                the destination
     * @param value
     *                the distinguish name value
     */
    public final void mapDistinguishName(top t, IBean destBean,
            final String value) {
        // Map nothing except the dn !
        destBean.setDistinguishName(value);
    }

    /**
     * Set a bean from an LDAP entry
     *
     * @param entry
     *                the LDAP entry
     * @param baseDn
     *                the base Dn used to set the right Dn
     * @param c
     *                class to instantiate
     * @return the bean
     * @throws NamingException
     *                 thrown if a directory exception is encountered while
     *                 looking at the entry
     */
    public static AbstractBean getInstance(final SearchResult entry,
            final String baseDn, final Class<?> c) throws NamingException {
        try {
            if (entry != null) {
                AbstractBean ab = (AbstractBean) c.newInstance();
                String dn = entry.getName();

                if ((dn.length() > 0) && (dn.charAt(0) == '"') &&
                        (dn.charAt(dn.length() - 1) == '"')) {
                    dn = dn.substring(1, dn.length() - 1);
                }

                if ((baseDn != null) && (baseDn.length() > 0)) {
                    if (dn.length() > 0) {
                        ab.setDistinguishName(dn + "," + baseDn);
                    } else {
                        ab.setDistinguishName(baseDn);
                    }
                } else {
                    ab.setDistinguishName(dn);
                }

                NamingEnumeration<?> ne = entry.getAttributes().getAll();

                while (ne.hasMore()) {
                    ab.setAttribute((Attribute) ne.next());
                }

                return ab;
            } else {
                return null;
            }
        } catch (InstantiationException ie) {
            LOGGER.error(ie, ie);
        } catch (IllegalAccessException iae) {
            LOGGER.error(iae, iae);
        }

        return null;
    }

    /**
     * Get an attribute from its name.
     *
     * @param id the name
     * @return the LDAP attribute
     */
    public final Attribute getAttributeById(final String id) {
        // use lower case since attribute names are case-insensitive
        return attrs.get(id.toLowerCase());
    }

    /**
     * Get the <b>first</b> value of an attribute from its name
     * 
     * @param id the name
     * @return the LDAP attribute
     * @throws NamingException
     */
    public final String getAttributeValueById(final String id) throws NamingException {
        // use lower case since attribute names are case-insensitive
        Attribute attribute = attrs.get(id.toLowerCase());
        String result = "";
        if (attribute != null && attribute.size() > 0) {
            result = attribute.get(0).toString();
        }
        return result;
    }


    /**
     * Get the attributes name list.
     *
     * @return the attributes list
     */
    public final Set<String> getAttributesNames() {
        return attrs.keySet();
    }

    /**
     * Set an attribute.
     *
     * @param attr
     *                the attribute to set
     */
    public final void setAttribute(final Attribute attr) {
        // use lower case since attribute names are case-insensitive
        attrs.put(attr.getID().toLowerCase(), attr);
    }

    /**
     * Default distinguish name getter.
     *
     * @return the distinguishName
     */
    public final String getDistinguishName() {
        return distinguishName;
    }
    
    /**
     * Distinguish name getter that makes sure to return the FULL DN (including suffix).
     *
     * @return the distinguishName
     */
    public final String getFullDistinguishedName() {
    	if (!distinguishName.endsWith("," + Configuration.DN_REAL_ROOT)) {
    		return distinguishName + "," + Configuration.DN_REAL_ROOT;
    	} else {
    		return distinguishName;
    	}
    }
    
    
    

    /**
     * Default distinguishName setter.
     *
     * @param dn
     *                the distinguishName to set
     */
    public final void setDistinguishName(final String dn) {
        distinguishName = dn;
    }
    
    public void generateDn() throws NamingException {}

    /**
     * Bean pretty printer.
     *
     * @return the pretty formatted string to display
     */
    public final String toString() {
        StringBuffer sb = new StringBuffer();
        Iterator<String> keySetIter = attrs.keySet().iterator();
        sb.append("dn: ").append(distinguishName).append('\n');

        while (keySetIter.hasNext()) {
            String key = keySetIter.next();

            if (attrs.get(key) != null) {
                sb.append("=> " + key);

                Object values = attrs.get(key);

                if (values instanceof List) {
                    Iterator<?> valuesIter = ((List<?>) values).iterator();

                    while (valuesIter.hasNext()) {
                        sb.append(" - ").append(valuesIter.next()).append("\n");
                    }
                } else {
                    sb.append(" - ").append(values).append("\n");
                }
            }
        }

        return sb.toString();
    }

    public static final Map<String, Method> getLocalMethods() {
        return localMethods;
    }

    public static final void setLocalMethods(Map<String, Method> localMethods) {
        AbstractBean.localMethods = localMethods;
    }
}
