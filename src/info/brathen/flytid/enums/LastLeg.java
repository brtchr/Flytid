/**
 * 
 */
package info.brathen.flytid.enums;

/**
 * @author Christoffer
 *
 */
public enum LastLeg {
	D("Domestic"),
	S("Schengen"),
	I("International");
	
	private final String name;
	
	private LastLeg(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
