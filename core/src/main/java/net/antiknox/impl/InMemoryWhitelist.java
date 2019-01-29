package net.antiknox.impl;

import net.antiknox.Whitelist;

import java.util.HashSet;
import java.util.Set;

public class InMemoryWhitelist implements Whitelist {

	private Set<String> whitelist = new HashSet<>();

	@Override
	public boolean contains(String ip) {
		return whitelist.contains(ip);
	}

	@Override
	public void add(String ip) {
		whitelist.add(ip);
	}

}
