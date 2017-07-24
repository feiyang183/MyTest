package com.example.dell.myscada.model;


/**
 * Created by Dell on 2017/3/9.
 */


public class mEnergy {
    private String id;
    private double state;
    private double temperature;
    private double capacity;
    private double speed;
    private String date;

    private String Lruntime;
    private String Lstoptime;
    private String Lusef;
    private String Mruntime;
    private String Mstoptime;
    private String Musef;
    private String Truntime;
    private String Tstoptime;
    private String Tusef;

    private String AllCap;
    private String YearCap;
    private String MonthCap;
    private String MonthCapL;
    private String DayCap;
    private String HourCap;

    public mEnergy(){
        this.id = null;
        this.state = 0;
        this.capacity = 0;
        this.speed = 0;
        this.temperature = 0;
        this.date = null;
        this.Lruntime = null;
        this.Lstoptime = null;
        this.Lusef = null;
        this.Mruntime = null;
        this.Mstoptime = null;
        this.Musef = null;
        this.Truntime = null;
        this.Tstoptime = null;
        this.Tusef = null;
        this.AllCap = null;
        this.YearCap = null;
        this.MonthCap = null;
        this.MonthCapL = null;
        this.DayCap = null;
        this.HourCap = null;
    }

    public mEnergy(String id,int state, String capacity, String speed, String temperature, String date){
        setId(id);
        setState(state);
        setCapacity(capacity);
        setSpeed(speed);
        setTemperature(temperature);
        setDate(date);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(double state) {
        this.state = state;
    }

    public void setState(String state){
        this.state = Double.valueOf(state);
    }

    public void setCapacity(String capacity) {
        this.capacity = Double.valueOf(capacity);
    }

    public void setCapacity(double capacity){
        this.capacity = capacity;
    }

    public void setSpeed(String speed) {
        this.speed = Double.valueOf(speed);
    }

    public void setSpeed(double speed){
        this.speed = speed;
    }

    public void setTemperature(String temperature) {
        this.temperature = Double.valueOf(temperature);
    }

    public void setTemperature(double temperature){
        this.temperature = temperature;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLruntime(String lruntime) {
        this.Lruntime = lruntime;
    }

    public void setLstoptime(String lstoptime) {
        this.Lstoptime = lstoptime;
    }

    public void setLusef(String lusef) {
        this.Lusef = lusef;
    }

    public void setMruntime(String mruntime) {
        this.Mruntime = mruntime;
    }

    public void setMstoptime(String mstoptime) {
        this.Mstoptime = mstoptime;
    }

    public void setMusef(String musef) {
        this.Musef = musef;
    }

    public void setTruntime(String truntime) {
        this.Truntime = truntime;
    }

    public void setTstoptime(String tstoptime) {
        this.Tstoptime = tstoptime;
    }

    public void setTusef(String tusef) {
        this.Tusef = tusef;
    }

    public void setAllCap(String allCap) {
        this.AllCap = allCap;
    }

    public void setYearCap(String yearCap) {
        this.YearCap = yearCap;
    }

    public void setMonthCap(String monthCap) {
        this.MonthCap = monthCap;
    }

    public void setMonthCapL(String monthCapL) {
        this.MonthCapL = monthCapL;
    }

    public void setDayCap(String dayCap) {
        this.DayCap = dayCap;
    }

    public void setHourCap(String hourCap) {
        this.HourCap = hourCap;
    }

    public String getId() {
        return id;
    }

    public double getState() {
        return state;
    }

    public double getCapacity() {
        return capacity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getDate() {
        return date;
    }

    public String getLruntime() {
        return Lruntime;
    }

    public String getLstoptime() {
        return Lstoptime;
    }

    public String getLusef() {
        return Lusef;
    }

    public String getMruntime() {
        return Mruntime;
    }

    public String getMstoptime() {
        return Mstoptime;
    }

    public String getMusef() {
        return Musef;
    }

    public String getTruntime() {
        return Truntime;
    }

    public String getTstoptime() {
        return Tstoptime;
    }

    public String getTusef() {
        return Tusef;
    }

    public String getAllCap() {
        return AllCap;
    }

    public String getYearCap() {
        return YearCap;
    }

    public String getMonthCap() {
        return MonthCap;
    }

    public String getMonthCapL() {
        return MonthCapL;
    }

    public String getDayCap() {
        return DayCap;
    }

    public String getHourCap() {
        return HourCap;
    }
}
