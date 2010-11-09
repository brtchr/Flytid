package info.brathen.flytid.enums;

/**
 * @author Christoffer
 *
 */
public enum UrlParameters {
	
	DIRECTION("direction"),
	AIRPORT("airport");
	
	final private String param;

	private UrlParameters(String param) {
		this.param = param;
	}

	/**
	 * @return the param
	 */
	public String getParam() {
		return param;
	}

	public String getUrlParam(String value) {
		return this.getParam() + "=" + value + "&";
	}
}
