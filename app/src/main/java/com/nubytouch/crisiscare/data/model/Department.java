package com.nubytouch.crisiscare.data.model;


public class Department
{
    public final String id;
    public final String name;

    public Department(String id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
