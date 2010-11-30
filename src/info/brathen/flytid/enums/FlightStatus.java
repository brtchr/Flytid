/**
 * 
 */
package info.brathen.flytid.enums;

/**
 * @author Christoffer
 *
 */
public enum FlightStatus {
	N("New info", "Ny info"),
	E("New time", "Ny tid"),
	D("Departed", "Avreist"),
	A("Arrived", "Landet"),
	C("Cancelled","Innstilt");
	
	private String textEn;
	private String textNo;
	
	/**
	 * @param textEn
	 * @param textNo
	 */
	private FlightStatus(String textEn, String textNo) {
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
	 * @param textEn the textEn to set
	 */
	public void setTextEn(String textEn) {
		this.textEn = textEn;
	}
	/**
	 * @return the textNo
	 */
	public String getTextNo() {
		return textNo;
	}
	/**
	 * @param textNo the textNo to set
	 */
	public void setTextNo(String textNo) {
		this.textNo = textNo;
	}
	
	
}
