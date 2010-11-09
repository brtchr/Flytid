/**
 * 
 */
package info.brathen.flytid.enums;

/**
 * @author Christoffer
 *
 */
public enum ArrDep {
	D("Departure", "Avgang"),
	A("Arrival", "Ankomst");
	
	private final String textEn;
	private final String textNo;
	/**
	 * @param textEn
	 * @param textNo
	 */
	private ArrDep(String textEn, String textNo) {
		this.textEn = textEn;
		this.textNo = textNo;
	}
	/**
	 * @return the textEn
	 */
	public String getTextEn() {
		return textEn;
	}
	/**
	 * @return the textNo
	 */
	public String getTextNo() {
		return textNo;
	}
	
}
