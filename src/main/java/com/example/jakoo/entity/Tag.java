package com.example.jakoo.entity;

public class Tag {
    private Long tagID;
    private String name;
    private Boolean isRelateTo;

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    public Long getTagID() {
        return tagID;
    }

    public void setTagID(Long tagID) {
        this.tagID = tagID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRelateTo() {
        return isRelateTo;
    }

    public void setRelateTo(Boolean relateTo) {
        isRelateTo = relateTo;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }
}
