package com.space.model;


import java.io.Serializable;

public class ShipFilterParam implements Serializable {
    private Integer pageNumber;
    private Integer pageSize;
    private String order;
    private Long after;
    private Long before;
    private Double minSpeed;
    private Double maxSpeed;
    private Integer minCrewSize;
    private Integer maxCrewSize;
    private Double minRating;
    private Double maxRating;
    private String name;
    private String planet;
    private ShipType shipType;
    private Boolean isUsed;


    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public Long getAfter() {
        return after;
    }

    public void setAfter(Long after) {
        this.after = after;
    }

    public Long getBefore() {
        return before;
    }

    public void setBefore(Long before) {
        this.before = before;
    }

    public Double getMinSpeed() {
        return minSpeed;
    }

    public void setMinSpeed(Double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public Double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public Integer getMinCrewSize() {
        return minCrewSize;
    }

    public void setMinCrewSize(Integer minCrewSize) {
        this.minCrewSize = minCrewSize;
    }

    public Integer getMaxCrewSize() {
        return maxCrewSize;
    }

    public void setMaxCrewSize(Integer maxCrewSize) {
        this.maxCrewSize = maxCrewSize;
    }

    public Double getMinRating() {
        return minRating;
    }

    public void setMinRating(Double minRating) {
        this.minRating = minRating;
    }

    public Double getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(Double maxRating) {
        this.maxRating = maxRating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Boolean isUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }
}
