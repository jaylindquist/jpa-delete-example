package com.bitwiseor.jpa.example;

import javax.persistence.*;

@Entity
@Table(name = "data_table")
public class DataEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private Long first;

    @Column(unique = true)
    private Long last;

    private String meta;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFirst() {
        return first;
    }

    public void setFirst(Long first) {
        this.first = first;
    }

    public Long getLast() {
        return last;
    }

    public void setLast(Long last) {
        this.last = last;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }
}
