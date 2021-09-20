package com.example.myapplication;

public class ModelPatientData {
    String Patient_Name , Patient_Email , isChecked , Appointed_Day , Appointed_Doctor;

    public ModelPatientData() {
    }

    public ModelPatientData(String patient_Name, String patient_Email, String isChecked, String appointed_Day, String appointed_Doctor) {
        Patient_Name = patient_Name;
        Patient_Email = patient_Email;
        this.isChecked = isChecked;
        Appointed_Day = appointed_Day;
        Appointed_Doctor = appointed_Doctor;
    }

    public String getPatient_Name() {
        return Patient_Name;
    }

    public void setPatient_Name(String patient_Name) {
        Patient_Name = patient_Name;
    }

    public String getPatient_Email() {
        return Patient_Email;
    }

    public void setPatient_Email(String patient_Email) {
        Patient_Email = patient_Email;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getAppointed_Day() {
        return Appointed_Day;
    }

    public void setAppointed_Day(String appointed_Day) {
        Appointed_Day = appointed_Day;
    }

    public String getAppointed_Doctor() {
        return Appointed_Doctor;
    }

    public void setAppointed_Doctor(String appointed_Doctor) {
        Appointed_Doctor = appointed_Doctor;
    }
}
