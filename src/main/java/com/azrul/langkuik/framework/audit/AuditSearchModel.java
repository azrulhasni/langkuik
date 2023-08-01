/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.audit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 *
 * @author azrul
 */
public class AuditSearchModel {
    private Long id;
    private String username;
    private LocalDateTime from;
    private LocalDateTime to;

    public AuditSearchModel(){
        id=null;
        username=null;
        from=null;
        to=null;
    }
//    /**
//     * @return the id
//     */
//    public Optional<String> getId() {
//        return id;
//    }
//
//    /**
//     * @param id the id to set
//     */
//    public void setId(String id) {
//        this.id = Optional.ofNullable(id);
//    }
//
//    /**
//     * @return the username
//     */
//    public Optional<String> getUsername() {
//        return username;
//    }
//
//    /**
//     * @param username the username to set
//     */
//    public void setUsername(String username) {
//        this.username = Optional.ofNullable(username);
//    }
//
//    /**
//     * @return the from
//     */
//    public Optional<LocalDate> getFrom() {
//        return from;
//    }
//
//    /**
//     * @param from the from to set
//     */
//    public void setFrom(LocalDate from) {
//        this.from = Optional.ofNullable(from);
//    }
//
//    /**
//     * @return the to
//     */
//    public Optional<LocalDate> getTo() {
//        return to;
//    }
//
//    /**
//     * @param to the to to set
//     */
//    public void setTo(LocalDate to) {
//        this.to = Optional.ofNullable(to);
//    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the from
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(LocalDateTime from) {
        this.from = from;
    }

    /**
     * @return the to
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * @param to the to to set
     */
    public void setTo(LocalDateTime to) {
        this.to = to;
    }
}
