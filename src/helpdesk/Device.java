package helpdesk;
public class Device {
    private String type;
    private String model;
    private String serialNumber;
    public Device(String type, String model, String serialNumber) {
        this.type = type;
        this.model = model;
        this.serialNumber = serialNumber;
    }
    public String getType() {
        return type;
    }
    public String getModel() {
        return model;
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    @Override
    public String toString() {
        return type + " | " + model
                + " | Серийный номер: " + serialNumber;
    }
}
