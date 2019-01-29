package net.antiknox;

public interface Whitelist {

	boolean contains(String ip);

	void add(String ip);

}
