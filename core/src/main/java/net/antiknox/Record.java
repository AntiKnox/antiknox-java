package net.antiknox;

public class Record {

	public static final Record EMPTY = new Record();

	private boolean match;
	private Direct direct;
	private Heuristics heuristics = new Heuristics();
	private String error;
	private int credits;

	public boolean hasDirectMatch() {
		return match && direct != null;
	}

	public Direct getDirectMatch() {
		return direct;
	}

	public Heuristics getHeuristics() {
		return heuristics;
	}

	public boolean hasError() {
		return error != null;
	}

	public String getError() {
		return error;
	}

	public int getCredits() {
		return credits;
	}

	static class Direct {

		private Type type;
		private String provider;

		public Type getType() {
			return type;
		}

		public String getProvider() {
			return provider;
		}

	}

	static class Heuristics {

		private String company = "";
		private String label = "";

	}

	enum Type {
		VPN,
		PROXY,
		TOR
	}

}
