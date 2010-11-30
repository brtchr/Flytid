package info.brathen.flytid.domain;

/**
 * Domain class for fligth companies
 * 
 * @author Christoffer
 * 
 */
public class Airline implements Comparable<Airline> {
	private String code;
	private String name;

	/**
	 * Empty constructor
	 */
	public Airline() {
		super();
	}

	/**
	 * Constructor with fields
	 * 
	 * @param code
	 * @param name
	 */
	public Airline(String code, String name) {
		super();
		this.code = code;
		this.name = name;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Airline another) {
		return this.getName().compareTo(another.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Airline) {
			return this.getCode().equals(((Airline) o).getCode());
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = 1;
		hash = hash * 31 + code.hashCode();
		return hash;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.getCode() + " - " + this.getName());
	}

}
