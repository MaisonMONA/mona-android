package com.example.dajc.tabs;

public class LieuCulturel {

    String Arrondissement;
    String NomReseau;
    String NomLieuCulturel;
    String Adresse;
    String CodePostal;
    String Ville;
    String Province;
    String Telephone;
    String SiteInternet;
    Double Longitude;
    Double Latitude;
    String Description;
    String etat;

    public LieuCulturel(String arrondissement, String nomReseau, String nomLieuCulturel, String adresse, String codePostal, String ville, String province, String telephone, String siteInternet, Double longitude, Double latitude, String description) {
        Arrondissement = arrondissement;
        NomReseau = nomReseau;
        NomLieuCulturel = nomLieuCulturel;
        Adresse = adresse;
        CodePostal = codePostal;
        Ville = ville;
        Province = province;
        Telephone = telephone;
        SiteInternet = siteInternet;
        Longitude = longitude;
        Latitude = latitude;
        Description = description;
        etat = "notvisited";
    }

    public void setEtat(String newEtat){
        etat = newEtat;
    }

    public String getEtat(){
        return etat;
    }
}
