package mysweethome.MSHbackend.Models;

public enum OutputDeviceType {
    AIR_CONDITIONER(1), LIGHT(0), TELEVISION(2), SPEAKER(3), DEHUMIDIFER(4);
    
    private Integer index;

    private OutputDeviceType(Integer index) { this.index = index; }

    public static OutputDeviceType valueOf(Integer devIndex) {
        for (OutputDeviceType l : OutputDeviceType.values()) {
            if (l.index == devIndex) return l;
        }
        throw new IllegalArgumentException("Device type not found.");
    }

    @Override
    public String toString() {
        return this.index.toString();
    }
}
