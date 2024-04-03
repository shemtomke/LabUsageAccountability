package com.example.labusageaccountability.Models;

public class ActivityRecord {
    private int id;
    private String clientName;
    private String purpose;
    private String startTime;
    private String endTime;

    public int getId()
    {
        return id;
    }
    public String getClientName()
    {
        return clientName;
    }
    public String getPurpose()
    {
        return purpose;
    }
    public String getStartTime()
    {
        return startTime;
    }
    public String getEndTime()
    {
        return endTime;
    }

    public ActivityRecord(String _clientName, String _purpose, String _startTime, String _endTime)
    {
        clientName = _clientName;
        purpose = _purpose;
        startTime = _startTime;
        endTime = _endTime;
    }
}
