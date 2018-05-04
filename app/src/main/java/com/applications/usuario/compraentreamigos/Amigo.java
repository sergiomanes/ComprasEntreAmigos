package com.applications.usuario.compraentreamigos;


import android.os.Parcel;
import android.os.Parcelable;

class Amigo implements Parcelable {

    private String name;
    private double hasToPay; //Si es positivo, le deben. Si es negativo, debe
    private double paid;
    private double discount;

    public Amigo(String name, double hasToPay, double paid, double discount) {
        this.name = name;
        this.hasToPay = hasToPay;
        this.paid = paid;
        this.discount = discount;
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

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    protected Amigo(Parcel in) {
        name = in.readString();
        hasToPay = in.readDouble();
        paid = in.readDouble();
        discount = in.readDouble();
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
        dest.writeDouble(discount);
    }

    @SuppressWarnings("unused")
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