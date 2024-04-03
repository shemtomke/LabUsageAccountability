package com.example.labusageaccountability.Models;

public class Staff {
    private int id;
    private String username;
    private String password;
    private String role;

    public int getId()
    {
        return id;
    }
    public String getRole()
    {
        return role;
    }
    public String getUsername()
    {
        return username;
    }
    public String getPassword()
    {
        return password;
    }
    public void setUsername(String userName)
    {
        username = userName;
    }
    public void setPassword(String passWord)
    {
        password = passWord;
    }
    public void setRole(String _role){role = _role;}
    public Staff(String userName, String passWord, String _role)
    {
        username = userName;
        password = passWord;
        role = _role;
    }
    public Staff(int _id, String userName, String passWord, String _role)
    {
        id = _id;
        username = userName;
        password = passWord;
        role = _role;
    }
    public Staff(String userName, String passWord)
    {
        username = userName;
        password = passWord;
    }
}
