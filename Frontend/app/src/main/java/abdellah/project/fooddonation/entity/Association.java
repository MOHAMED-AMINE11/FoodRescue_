package abdellah.project.fooddonation.entity;

public class Association {
    private int id;
    private String description;
    private String mail;

    private String nom;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    private String telephone;
    private String adresse;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // Default constructor
    public Association() {
    }

    // Parameterized constructor
    public Association(int idAssociation, String description, String mail, String nom, String imageUrl) {
        this.id = idAssociation;
        this.description = description;
        this.mail = mail;
        this.nom = nom;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters
    public int getIdAssociation() {
        return id;
    }

    public void setIdAssociation(int idAssociation) {
        this.id = idAssociation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }



    @Override
    public String toString() {
        return "Association{" +
                "idAssociation=" + id +
                ", description='" + description + '\'' +
                ", mail='" + mail + '\'' +
                ", nom='" + nom + '\'' +
                '}';
    }

}
