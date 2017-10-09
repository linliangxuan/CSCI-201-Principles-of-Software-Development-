package parser;

public class Meeting {
	
	private String type;
	private String section;
	private String room;
	private MeetingPeriod[] meetingPeriods;
	private Assistant[] assistants;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public MeetingPeriod[] getMeetingPeriods() {
		return meetingPeriods;
	}

	public void setMeetingPeriods(MeetingPeriod[] meetingPeriods) {
		this.meetingPeriods = meetingPeriods;
	}

	public Assistant[] getAssistants() {
		return assistants;
	}

	public void setAssistants(Assistant[] assistants) {
		this.assistants = assistants;
	}
	
}
