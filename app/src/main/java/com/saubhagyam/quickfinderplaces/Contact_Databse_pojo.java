package com.saubhagyam.quickfinderplaces;

public class Contact_Databse_pojo {


    String uname,uadd,ustatus,urate,uplaceid,ukm;

    public Contact_Databse_pojo() {
    }


    public Contact_Databse_pojo(String uname,String uplaceid,String uadd,String ustatus, String urate,String ukm) {
        this.uname = uname;
        this.uplaceid = uplaceid;
        this.uadd = uadd;
        this.ustatus = ustatus;
        this.urate = urate;
        this.ukm = ukm;

    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUadd() {
        return uadd;
    }

    public void setUadd(String uadd) {
        this.uadd = uadd;
    }

    public String getUstatus() {
        return ustatus;
    }

    public void setUstatus(String ustatus) {
        this.ustatus = ustatus;
    }

    public String getUrate() {
        return urate;
    }

    public void setUrate(String urate) {
        this.urate = urate;
    }

    public String getUplaceid() {
        return uplaceid;
    }

    public void setUplaceid(String uplaceid) {
        this.uplaceid = uplaceid;
    }

    public String getUkm() {
        return ukm;
    }

    public void setUkm(String ukm) {
        this.ukm = ukm;
    }
}
