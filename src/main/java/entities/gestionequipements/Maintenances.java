package entities.gestionequipements;

public class Maintenances {

    private int idm;

    private int ide;
    private String date_maintenance;
    private String status;

    public Maintenances() {
    }

    public Maintenances(int idm,int ide, String date_maintenance, String status) {
        this.idm= idm;
        this.ide=ide;
        this.date_maintenance = date_maintenance;
        this.status = status;
    }

    public int getIde() {
        return ide;
    }

    public void setIde(int ide) {
        this.ide = ide;
    }

    public int getIdm() {
        return idm;
    }

    public void setIdm(int idm) {
        this.idm = idm;
    }

    public String getDate_maintenance() {
        return date_maintenance;
    }

    public void setDate_maintenance(String date_maintenance) {
        this.date_maintenance = date_maintenance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Maintenances{" +
                "idm=" + idm +
                ", ide=" + ide +
                ", date_maintenance='" + date_maintenance + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
