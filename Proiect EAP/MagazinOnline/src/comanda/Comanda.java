package comanda;

public class Comanda {
    public static final String GATA_DE_LIVRARE="gata de livrare";
    public static final String LIVRARE_IN_CURS="livrare in curs";
    public static final String LIVRATA_CU_SUCCES="livrata cu succes";
    private String id_comanda;
    private String id_cos;
    private String adresa;
    private String data_crearii;
    private String status;
    private String livrator;

    public Comanda(String id_comanda, String id_cos, String adresa, String data_crearii, String status, String livrator) {
        this.id_comanda = id_comanda;
        this.id_cos = id_cos;
        this.adresa = adresa;
        this.data_crearii = data_crearii;
        this.status = status;
        this.livrator = livrator;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLivrator(String livrator) {
        this.livrator = livrator;
    }

    public Comanda() {
    }

    public String getId_comanda() {
        return id_comanda;
    }

    public String getId_cos() {
        return id_cos;
    }

    public String getAdresa() {
        return adresa;
    }

    public String getData_crearii() {
        return data_crearii;
    }

    public String getStatus() {
        return status;
    }

    public String getLivrator() {
        return livrator;
    }
}
