package com.applications.usuario.compraentreamigos.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Amigo implements Parcelable {

    private String name;
    private double hasToPay; //Si es positivo, le deben. Si es negativo, debe
    private double paid;

    public Amigo(String name, double hasToPay, double paid) {
        this.name = name;
        this.hasToPay = hasToPay;
        this.paid = paid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getHasToPay() {
        return hasToPay;
    }

    public void setHasToPay(double hasToPay) {
        this.hasToPay = hasToPay;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    protected Amigo(Parcel in) {
        name = in.readString();
        hasToPay = in.readDouble();
        paid = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(hasToPay);
        dest.writeDouble(paid);
    }

    public static final Parcelable.Creator<Amigo> CREATOR = new Parcelable.Creator<Amigo>() {
        @Override
        public Amigo createFromParcel(Parcel in) {
            return new Amigo(in);
        }

        @Override
        public Amigo[] newArray(int size) {
            return new Amigo[size];
        }
    };
}