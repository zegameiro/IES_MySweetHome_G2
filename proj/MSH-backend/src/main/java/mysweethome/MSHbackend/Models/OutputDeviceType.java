package mysweethome.MSHbackend.Models;

import java.util.Arrays;
import java.util.List;

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

     public List<String> getPossibleActions() {
        switch (this) {
            case TELEVISION:
                return Arrays.asList("Turn ON", "Turn OFF", "Change Channel", "Adjust Volume");
            case DEHUMIDIFER:
                return Arrays.asList("Turn ON", "Turn OFF", "Adjust Humidity level");
            case LIGHT:
                return Arrays.asList("Turn ON", "Turn OFF", "Change Color", "Adjust Brightness");
            case AIR_CONDITIONER:
                return Arrays.asList("Turn ON", "Turn OFF", "Adjust Temperature");
            case SPEAKER:
                return Arrays.asList("Turn ON", "Turn OFF", "Change Music", "Adjust Volume");
            default:
                throw new UnsupportedOperationException("Unsupported device type: " + this);
        }
    }
}
