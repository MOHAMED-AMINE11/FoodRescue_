package com.example.backend_pfm.beans;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "donations")
public class Donations implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Autoincrement id
    @Column(name = "id_donation")
    private int idDonation;

    @Column(name = "id_donner")
    private int idDonner;

    @Column(name = "title")
    private String title;

    @Column(name = "date")
    private String date;

    @Column(name = "food_type_group")
    private String foodTypeGroup;

    @Column(name = "quality_dropdown")
    private String qualityDropdown;

    @Column(name = "food_image_preview")
    private String foodImagePreview;

    @Column(name = "description")
    private String description;

    @Column(name = "donare_name")
    private String donareName;

    @Column(name = "is_closed")
    private boolean isClosed;
    @Column(name="uniter")
    private String uniter;

    @Column(name = "longitude")
    private float longitude;

    @Column(name = "latitude")
    private float latitude;

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    private float quantity;
    // Default constructor
    public Donations() {
    }

    // Constructor with specific parameters
    public Donations(String foodTypeGroup, String qualityDropdown, String foodImagePreview) {
        this.foodTypeGroup = foodTypeGroup;
        this.qualityDropdown = qualityDropdown;
        this.foodImagePreview = foodImagePreview;
    }

    // Full parameterized constructor
    public Donations(int idDonation, int idDonner, String foodTypeGroup, String qualityDropdown,
                     String foodImagePreview, String description, String donareName,
                     boolean isClosed, float longitude, float latitude) {
        this.idDonation = idDonation;
        this.idDonner = idDonner;
        this.foodTypeGroup = foodTypeGroup;
        this.qualityDropdown = qualityDropdown;
        this.foodImagePreview = foodImagePreview;
        this.description = description;
        this.donareName = donareName;
        this.isClosed = isClosed;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    // Getters and Setters
    public int getIdDonation() {
        return idDonation;
    }

    public void setIdDonation(int idDonation) {
        this.idDonation = idDonation;
    }

    public int getIdDonner() {
        return idDonner;
    }

    public void setIdDonner(int idDonner) {
        this.idDonner = idDonner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFoodTypeGroup() {
        return foodTypeGroup;
    }

    public void setFoodTypeGroup(String foodTypeGroup) {
        this.foodTypeGroup = foodTypeGroup;
    }

    public String getQualityDropdown() {
        return qualityDropdown;
    }

    public void setQualityDropdown(String qualityDropdown) {
        this.qualityDropdown = qualityDropdown;
    }

    public String getFoodImagePreview() {
        return foodImagePreview;
    }

    public void setFoodImagePreview(String foodImagePreview) {
        this.foodImagePreview = foodImagePreview;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDonareName() {
        return donareName;
    }

    public void setDonareName(String donareName) {
        this.donareName = donareName;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    // toString method for debugging and logging
    @Override
    public String toString() {
        return "Donations{" +
                "idDonation=" + idDonation +
                ", idDonner=" + idDonner +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", foodTypeGroup='" + foodTypeGroup + '\'' +
                ", qualityDropdown='" + qualityDropdown + '\'' +
                ", foodImagePreview='" + foodImagePreview + '\'' +
                ", description='" + description + '\'' +
                ", donareName='" + donareName + '\'' +
                ", isClosed=" + isClosed +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    public String getUniter() {
        return uniter;
    }

    public void setUniter(String uniter) {
        this.uniter = uniter;
    }
}
