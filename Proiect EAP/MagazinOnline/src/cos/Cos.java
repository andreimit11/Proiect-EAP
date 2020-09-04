package cos;


public class Cos {
    private String id_cos;
    private String proprietar;
    private String produs_cantitate; // id_produs-cantitate,
    private Double cost_total;

    public void setId_cos(String id_cos) {
        this.id_cos = id_cos;
    }

    public void setProprietar(String proprietar) {
        this.proprietar = proprietar;
    }

    public void setProdus_cantitate(String produs_cantitate) {
        this.produs_cantitate = produs_cantitate;
    }

    public void setCost_total(Double cost_total) {
        this.cost_total = cost_total;
    }

    public Cos(String id_cos, String proprietar, String produs_cantitate, Double cost_total) {
        this.id_cos = id_cos;
        this.proprietar = proprietar;
        this.produs_cantitate = produs_cantitate;
        this.cost_total = cost_total;
    }

    public Cos() {
    }

    public String getId_cos() {
        return id_cos;
    }

    public String getProprietar() {
        return proprietar;
    }

    public String getProdus_cantitate() {
        return produs_cantitate;
    }

    public Double getCost_total() {
        return cost_total;
    }
}
