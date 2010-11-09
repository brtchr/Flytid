/**
 * 
 */
package info.brathen.flytid.enums;

/**
 * @author Christoffer
 *
 */
public enum NorwegianAirports {
	OSL("Oslo"),
	TRD("Trondheim"),
	BGO("Bergen"),
	SVG("Stavanger"),
	KRS("Kristiansand"),
	TOS("Tromsø"),
	BOO("Bodø"),
//	RYG("Rygge"),
//	TRF("Sandefjord"),
	ALF("Alta"),
	ANX("Andenes"),
	BDU("Bardufoss"),
	BVG("Berlevåg"),
	BNN("Brønnøysund"),
	BJF("Båtsfjord"),
	VDB("Fagernes"),
	FRO("Florø"),
	FDE("Førde"),
	HFT("Hammerfest"),
	EVE("Harstad/Narvik"),
	HAA("Hasvik"),
	HAU("Haugesund"),
	HVG("Honningsvåg"),
	KKN("Kirkenes"),
	KSU("Kristiansund"),
	LKL("Lakselv"),
	LKN("Leknes"),
	MEH("Mehamn"),
	MQN("Mo i Rana"),
	MOL("Molde"),
	MJF("Mosjøen"),
	OSY("Namsos"),
	NVK("Narvik"),
	RRS("Røros"),
	RVK("Rørvik"),
	RET("Røst"),
	SDN("Sandane"),
	SSJ("Sandnessjøen"),
	SOG("Sogndal"),
	SKN("Stokmarknes"),
	SYG("Svalbard"),
	SVJ("Svolvær"),
	SOJ("Sørkjosen"),
	VDS("Vadsø"),
	VAW("Vardø"),
	VRY("Værøy"),
	HOV("Ørsta-Volda"),
	AES("Ålesund");
	
	private String navn;

	/**
	 * @param navn
	 */
	private NorwegianAirports(String navn) {
		this.navn = navn;
	}

	/**
	 * @return the navn
	 */
	public String getNavn() {
		return navn;
	}

	public static String[] getNames() {
		NorwegianAirports[] norwegianAirports = NorwegianAirports.values();
		String[] airportCodes = new String[norwegianAirports.length];
		for(int i = 0; i < norwegianAirports.length; i++) {
			airportCodes[i] = norwegianAirports[i].name();
		}
		return airportCodes;
	}
	
	public static String[] getFullNames() {
		NorwegianAirports[] norwegianAirports = NorwegianAirports.values();
		String[] airportNames = new String[norwegianAirports.length];
		for(int i = 0; i< norwegianAirports.length; i++) {
			airportNames[i] = norwegianAirports[i].toString();
		}
		return airportNames;
	}
	
	public static String getSQLString() {
		String sql = "(";
		for(NorwegianAirports norwegianAirport: NorwegianAirports.values()) {
			sql += "'" + norwegianAirport.name() + "', ";
		}
		sql = sql.substring(0, sql.length()-2);
		sql += ")";
		return sql;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.getNavn();
	}
}
