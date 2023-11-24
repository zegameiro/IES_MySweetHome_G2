package mysweethome.MSHbackend.Models;

public enum PossibleActions {
    TURN_ON(OutputDeviceType.LIGHT, OutputDeviceType.SPEAKER),
    TURN_OFF(OutputDeviceType.LIGHT, OutputDeviceType.SPEAKER),
    SET_TEMPERATURE(OutputDeviceType.AIR_CONDITIONER),
    CHANGE_CHANNEL(OutputDeviceType.TELEVISION);

    private final OutputDeviceType[] applicableDeviceTypes;

    PossibleActions(OutputDeviceType... applicableDeviceTypes) {
        this.applicableDeviceTypes = applicableDeviceTypes;
    }

    public boolean isApplicableTo(OutputDeviceType deviceType) {
        for (OutputDeviceType applicableType : applicableDeviceTypes) {
            if (applicableType == deviceType) {
                return true;
            }
        }
        return false;
    }
}