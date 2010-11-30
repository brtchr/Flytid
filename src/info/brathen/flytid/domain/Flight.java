/**
 * 
 */
package info.brathen.flytid.domain;

import info.brathen.flytid.enums.ArrDep;
import info.brathen.flytid.enums.FlightStatus;
import info.brathen.flytid.enums.LastLeg;
import info.brathen.flytid.util.DateFormatter;

import java.util.Date;

/**
 * @author Christoffer
 * 
 */
public class Flight {
	private String flightId;
	private LastLeg lastLeg;
	private Date scheduledTime;
	private ArrDep arrDep;
	private Airport airport;
	private Airport viaAirport;
	private Airline airline;
	private String checkInArea;
	private String gate;
	private FlightStatus flightstatus;
	private Date statusTime;
	private String baggageBand;

	/**
	 * Empty constructor 
	 */
	public Flight() {
		super();
	}

	/**
	 * @return the flightId
	 */
	public String getFlightId() {
		return flightId;
	}

	/**
	 * @param flightId the flightId to set
	 */
	public void setFlightId(String flightId) {
		this.flightId = flightId;
	}

	/**
	 * @return the lastLeg
	 */
	public LastLeg getLastLeg() {
		return lastLeg;
	}

	/**
	 * @param lastLeg the lastLeg to set
	 */
	public void setLastLeg(LastLeg lastLeg) {
		this.lastLeg = lastLeg;
	}

	/**
	 * @return the scheduledTime
	 */
	public Date getScheduledTime() {
		return scheduledTime;
	}

	/**
	 * @param scheduledTime the scheduledTime to set
	 */
	public void setScheduledTime(Date scheduledTime) {
		this.scheduledTime = scheduledTime;
	}

	/**
	 * @return the arrDep
	 */
	public ArrDep getArrDep() {
		return arrDep;
	}

	/**
	 * @param arrDep the arrDep to set
	 */
	public void setArrDep(ArrDep arrDep) {
		this.arrDep = arrDep;
	}

	/**
	 * @return the airport
	 */
	public Airport getAirport() {
		return airport;
	}

	/**
	 * @param airport the airport to set
	 */
	public void setAirport(Airport airport) {
		this.airport = airport;
	}

	/**
	 * @param viaAirport the viaAirport to set
	 */
	public void setViaAirport(Airport viaAirport) {
		this.viaAirport = viaAirport;
	}

	/**
	 * @return the viaAirport
	 */
	public Airport getViaAirport() {
		return viaAirport;
	}

	/**
	 * @return the airline
	 */
	public Airline getAirline() {
		return airline;
	}

	/**
	 * @param airline the airline to set
	 */
	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	/**
	 * @return the checkInArea
	 */
	public String getCheckInArea() {
		return checkInArea;
	}

	/**
	 * @param checkInArea the checkInArea to set
	 */
	public void setCheckInArea(String checkInArea) {
		this.checkInArea = checkInArea;
	}

	/**
	 * @return the gate
	 */
	public String getGate() {
		return gate;
	}

	/**
	 * @param gate the gate to set
	 */
	public void setGate(String gate) {
		this.gate = gate;
	}

	/**
	 * @return the flightstatus
	 */
	public FlightStatus getFlightStatus() {
		return flightstatus;
	}

	/**
	 * @param flightstatus the flightstatus to set
	 */
	public void setFlightStatus(FlightStatus flightstatus) {
		this.flightstatus = flightstatus;
	}

	/**
	 * @return the statusTime
	 */
	public Date getStatusTime() {
		return statusTime;
	}

	/**
	 * @param date the statusTime to set
	 */
	public void setStatusTime(Date date) {
		this.statusTime = date;
	}

	/**
	 * @return the baggageBand
	 */
	public String getBaggageBand() {
		return baggageBand;
	}

	/**
	 * @param baggageBand the baggageBand to set
	 */
	public void setBaggageBand(String baggageBand) {
		this.baggageBand = baggageBand;
	}

	public String getRemark() {
		StringBuilder remark = new StringBuilder();
		if(flightstatus != null) {
			remark.append(flightstatus.getTextNo());
			
			if(statusTime != null) {
				remark.append(" ");
				remark.append(DateFormatter.displayDate(statusTime));
			}
		}
		return remark.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((flightId == null) ? 0 : flightId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Flight other = (Flight) obj;
		if (flightId == null) {
			if (other.flightId != null)
				return false;
		} else if (!flightId.equals(other.flightId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Flight [flightId=" + flightId + ", scheduledTime=" + scheduledTime + ", airport=" + airport
				+ ", airline=" + airline + "\n, flightstatus=" + flightstatus + ", statusTime=" + statusTime + "]";
	}
	
	
	
}
