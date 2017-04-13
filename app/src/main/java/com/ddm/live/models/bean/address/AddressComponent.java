package com.ddm.live.models.bean.address;

import java.util.HashMap;
import java.util.Map;

public class AddressComponent {

    private String city;
    private String country;
    private String direction;
    private String distance;
    private String district;
    private String province;
    private String street;
    private String streetNumber;
    private Integer countryCode;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city The city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return The country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country The country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * @return The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * @return The distance
     */
    public String getDistance() {
        return distance;
    }

    /**
     * @param distance The distance
     */
    public void setDistance(String distance) {
        this.distance = distance;
    }

    /**
     * @return The district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district The district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return The province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province The province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return The street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street The street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * @return The streetNumber
     */
    public String getStreetNumber() {
        return streetNumber;
    }

    /**
     * @param streetNumber The street_number
     */
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    /**
     * @return The countryCode
     */
    public Integer getCountryCode() {
        return countryCode;
    }

    /**
     * @param countryCode The country_code
     */
    public void setCountryCode(Integer countryCode) {
        this.countryCode = countryCode;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

