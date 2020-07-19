package com.in28minutes.rest.webservices.restfulwebservices.versionning;

public class PersonV1 {
    private String name;

    public String getName() {
        return this.name;
    }

    public PersonV1(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
