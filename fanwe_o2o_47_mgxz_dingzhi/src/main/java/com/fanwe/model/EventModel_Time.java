package com.fanwe.model;

public class EventModel_Time {
	/*
	 * "special": { "time_begin": "1452546000", "time_end": "1452578400",
	 * "time_request": 1452595130, "time_local_request": null,
	 */

	private String time_begin;

	private String time_end;

	private String time_request;

	private String time_local_request;

	public String getTime_begin() {
		return time_begin;
	}

	public void setTime_begin(String time_begin) {
		this.time_begin = time_begin;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTime_request() {
		return time_request;
	}

	public void setTime_request(String time_request) {
		this.time_request = time_request;
	}

	public String getTime_local_request() {
		return time_local_request;
	}

	public void setTime_local_request(String time_local_request) {
		this.time_local_request = time_local_request;
	}

}
