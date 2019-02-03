package com.example.rohan.ankitproject;

public class windowdetails {
    public double[] initialvector,max2,max3,finalvec;
    public int state2year,state3year,finalyear,startyear;

    public windowdetails(double[] initialvector, double[] max2, double[] max3, double[] finalvec, int state2year, int state3year, int finalyear, int startyear) {
        this.initialvector = initialvector;
        this.max2 = max2;
        this.max3 = max3;
        this.finalvec = finalvec;
        this.state2year = state2year;
        this.state3year = state3year;
        this.finalyear = finalyear;
        this.startyear = startyear;
    }

    public double[] getInitialvector() {
        return initialvector;
    }

    public void setInitialvector(double[] initialvector) {
        this.initialvector = initialvector;
    }

    public double[] getMax2() {
        return max2;
    }

    public void setMax2(double[] max2) {
        this.max2 = max2;
    }

    public double[] getMax3() {
        return max3;
    }

    public void setMax3(double[] max3) {
        this.max3 = max3;
    }

    public double[] getFinalvec() {
        return finalvec;
    }

    public void setFinalvec(double[] finalvec) {
        this.finalvec = finalvec;
    }

    public int getState2year() {
        return state2year;
    }

    public void setState2year(int state2year) {
        this.state2year = state2year;
    }

    public int getState3year() {
        return state3year;
    }

    public void setState3year(int state3year) {
        this.state3year = state3year;
    }

    public int getFinalyear() {
        return finalyear;
    }

    public void setFinalyear(int finalyear) {
        this.finalyear = finalyear;
    }

    public int getStartyear() {
        return startyear;
    }

    public void setStartyear(int startyear) {
        this.startyear = startyear;
    }
}
