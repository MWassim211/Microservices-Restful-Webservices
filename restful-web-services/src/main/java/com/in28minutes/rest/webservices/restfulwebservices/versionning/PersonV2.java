package com.in28minutes.rest.webservices.restfulwebservices.versionning;

public class PersonV2 {
    private Name name;
    public Name getName() {
        return this.name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public PersonV2(Name name) {
        this.name = name;
    }
}
