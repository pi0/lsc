package org.lsc.configuration.objects.services;

import org.lsc.jndi.SimpleJndiDstService;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("ldapDestinationService")
public class DstLdap extends Ldap {

	@Override
	public Class<?> getImplementation() {
		return SimpleJndiDstService.class;
	}
}