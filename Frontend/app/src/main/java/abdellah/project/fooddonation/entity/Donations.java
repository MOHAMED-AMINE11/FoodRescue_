package abdellah.project.fooddonation.entity;

import android.widget.ImageView;

import java.io.Serializable;
import java.util.Date;

public class Donations implements Serializable {
    private int idDonation;
    private int idDonner;
    private String title;
    private Date date;
    private String  foodTypeGroup;
    private String qualityDropdown;
    private float quantity;
    private String uniter;
    private String  foodImagePreview;

    public Donations(float quantity) {

        this.quantity = quantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public Donations(String uniter,int doner_id,String title, Date date, String foodTypeGroup, String qualityDropdown, float quantity, String foodImagePreview, String description, String donareName, boolean isClosed, Double longitude, Double latitude) {
        this.title = title;
        this.date = date;
        this.foodTypeGroup = foodTypeGroup;
        this.qualityDropdown = qualityDropdown;
        this.quantity = quantity;
        this.foodImagePreview = foodImagePreview;
        this.description = description;
        this.donareName = donareName;
        this.isClosed = isClosed;
        this.longitude = longitude;
        this.latitude = latitude;
        this.uniter=uniter;
        this.idDonner=doner_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String description;
    private String donareName;
    private boolean isClosed;

    private Double longitude;
    private Double latitude;

    // Default constructor
    public Donations(String foodTypeGroup, String qualityDropdown, float quantity, String foodImagePreview) {
        this.foodTypeGroup = foodTypeGroup;
        this.qualityDropdown = qualityDropdown;
        this.quantity = quantity;
        this.foodImagePreview = foodImagePreview;
    }

    // Parameterized constructor


    // Getters and Setters
    public int getIdDonation() {
        return idDonation;
    }



    public int getIdDonner() {
        return idDonner;
    }

    public void setIdDonner(int idDonner) {
        this.idDonner = idDonner;
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

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    // toString method for debugging and logging
    @Override
    public String toString() {
        return "Donations{" +
                "idDonation=" + idDonation +
                ", idDonner=" + idDonner +
                ", description='" + description + '\'' +
                ", donareName='" + donareName + '\'' +
                ", isClosed=" + isClosed +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
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

    public String getUniter() {
        return uniter;
    }

    public void setUniter(String uniter) {
        this.uniter = uniter;
    }

    public float getQuantity() {
        return quantity;
    }
}
