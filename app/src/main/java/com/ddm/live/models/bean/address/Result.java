package com.ddm.live.models.bean.address;

public class Result {

    private Location location;
    private String formatted_address;
    private String business;
    private AddressComponent addressComponent;

    private String sematicDescription;
    private Integer cityCode;


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public AddressComponent getAddressComponent() {
        return addressComponent;
    }

    public void setAddressComponent(AddressComponent addressComponent) {
        this.addressComponent = addressComponent;
    }

    public String getSematicDescription() {
        return sematicDescription;
    }

    public void setSematicDescription(String sematicDescription) {
        this.sematicDescription = sematicDescription;
    }

    public Integer getCityCode() {
        return cityCode;
    }

    public void setCityCode(Integer cityCode) {
        this.cityCode = cityCode;
    }
}