package ru.mirea.panin.mireaproject.ui.networkdata;

public class IpInfo {

    private String ip, city, country, loc, org;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getIp() {
        return ip;
    }

    public String getLoc() {
        return loc;
    }

    public String getOrg() {
        return org;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    @Override
    public String toString() {
        return "ip: " + ip + '\n' +
                "city: " + city + '\n' +
                "country: " + country + '\n' +
                "location: " + loc + '\n' +
                "provider org.: " + org;
    }
}
