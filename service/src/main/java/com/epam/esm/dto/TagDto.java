package com.epam.esm.dto;

import java.io.Serializable;
import java.util.Objects;

public class TagDto implements Serializable {
    private static final long serialVersionUID = -7664123249924815397L;

    private String name;

    public TagDto() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(name, tagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "name='" + name + '\'' +
                '}';
    }
}
