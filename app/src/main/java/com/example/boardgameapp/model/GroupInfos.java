package com.example.boardgameapp.model;

public class GroupInfos {
    private final int groupID, createdBy, nextHost;
    private final String name, description, nextMeeting;
    private final Boolean isPollActive;

    public GroupInfos(int groupID, String name, String description, int createdBy, String nextMeeting, int nextHost, Boolean isPollActive) {
        this.groupID = groupID;
        this.createdBy = createdBy;
        this.nextHost = nextHost;
        this.name = name;
        this.description = description;
        this.nextMeeting = nextMeeting;
        this.isPollActive = isPollActive;
    }

    public int getGroupID() {
        return groupID;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public int getNextHost() {
        return nextHost;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getNextMeeting() {
        return nextMeeting;
    }

    public Boolean getPollActive() {
        return isPollActive;
    }
}
