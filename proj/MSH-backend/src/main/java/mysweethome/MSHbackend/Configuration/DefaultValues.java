package mysweethome.MSHbackend.Configuration;

import mysweethome.MSHbackend.Models.OutputDevice;
import mysweethome.MSHbackend.Models.OutputDeviceType;
import mysweethome.MSHbackend.Models.Room;
import mysweethome.MSHbackend.Repositories.OutputDeviceRepository;
import mysweethome.MSHbackend.Repositories.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultValues implements ApplicationRunner {

    @Autowired
    private OutputDeviceRepository outputDeviceRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {


        System.out.println("Inserted default output devices sucessfully! ");

        Room living_room = new Room();
        living_room.setName("Living Room");
        living_room.setFloorNumber(0);

        Room child_bedroom = new Room();
        child_bedroom.setName("Children Bedroom");
        child_bedroom.setFloorNumber(1);

        Room couple_bedroom = new Room();
        couple_bedroom.setName("Couple Bedroom");
        couple_bedroom.setFloorNumber(1);

        Room bath_room_1 = new Room();
        bath_room_1.setName("Bathroom 1");
        bath_room_1.setFloorNumber(0);

        Room bath_room_couple = new Room();
        bath_room_couple.setName("Bathroom in Couple Bedroom");
        bath_room_couple.setFloorNumber(1);

        Room kitchen = new Room();
        kitchen.setName("Kitchen");
        kitchen.setFloorNumber(0);

        Room attic = new Room();
        attic.setName("Attic");
        attic.setFloorNumber(2);

        roomRepository.save(kitchen);
        roomRepository.save(bath_room_couple);
        roomRepository.save(bath_room_1);
        roomRepository.save(child_bedroom);
        roomRepository.save(couple_bedroom);
        roomRepository.save(living_room);
        roomRepository.save(attic);

        System.out.println("Inserted default rooms sucessfully! ");

        System.out.println("Inserting default values into the Database");
        // Insert default values
        OutputDevice ceiling_light = new OutputDevice();
        ceiling_light.setName("Ceiling Light");
        ceiling_light.setDevice_category(OutputDeviceType.LIGHT);
        ceiling_light.setDevice_location(kitchen.getUid());
        ceiling_light.setCurrent_state("0");
        ceiling_light.setLaststatechange(System.currentTimeMillis());
        ceiling_light.setColor("white");

        outputDeviceRepository.save(ceiling_light);

        OutputDevice lamp_living = new OutputDevice();
        lamp_living.setName("Lamp");
        lamp_living.setDevice_category(OutputDeviceType.LIGHT);
        lamp_living.setDevice_location(living_room.getUid());
        lamp_living.setCurrent_state("1");
        lamp_living.setLaststatechange(System.currentTimeMillis());
        lamp_living.setColor("white");

        outputDeviceRepository.save(lamp_living);

        OutputDevice leds_ligits = new OutputDevice();
        leds_ligits.setName("LEDS Lights");
        leds_ligits.setDevice_category(OutputDeviceType.LIGHT);
        leds_ligits.setDevice_location(child_bedroom.getUid());
        leds_ligits.setCurrent_state("1");
        leds_ligits.setLaststatechange(System.currentTimeMillis());
        leds_ligits.setColor("red");

        outputDeviceRepository.save(leds_ligits);

        OutputDevice air_conditioner = new OutputDevice();
        air_conditioner.setName("Air Conditioner");
        air_conditioner.setDevice_category(OutputDeviceType.AIR_CONDITIONER);
        air_conditioner.setDevice_location(couple_bedroom.getUid());
        air_conditioner.setCurrent_state("1");
        air_conditioner.setLaststatechange(System.currentTimeMillis());
        air_conditioner.setTemperature(25);

        outputDeviceRepository.save(air_conditioner);

        OutputDevice attic_heater = new OutputDevice();
        attic_heater.setName("Heater");
        attic_heater.setDevice_category(OutputDeviceType.AIR_CONDITIONER);
        attic_heater.setDevice_location(attic.getUid());
        attic_heater.setCurrent_state("0");
        attic_heater.setLaststatechange(System.currentTimeMillis());
        attic_heater.setTemperature(20);

        outputDeviceRepository.save(attic_heater);

        OutputDevice tv_living = new OutputDevice();
        tv_living.setName("Television");
        tv_living.setDevice_category(OutputDeviceType.TELEVISION);
        tv_living.setDevice_location(living_room.getUid());
        tv_living.setCurrent_state("1");
        tv_living.setLaststatechange(System.currentTimeMillis());
        tv_living.setCurrent_channel("Fox");

        outputDeviceRepository.save(tv_living);

        OutputDevice tv_couple_bedr = new OutputDevice();
        tv_couple_bedr.setName("Tevelision");
        tv_couple_bedr.setDevice_category(OutputDeviceType.TELEVISION);
        tv_couple_bedr.setDevice_location(couple_bedroom.getUid());
        tv_couple_bedr.setCurrent_state("1");
        tv_couple_bedr.setLaststatechange(System.currentTimeMillis());
        tv_couple_bedr.setCurrent_channel("Axn HD");

        outputDeviceRepository.save(tv_couple_bedr);

        OutputDevice radio_living_room = new OutputDevice();
        radio_living_room.setName("Radio");
        radio_living_room.setDevice_category(OutputDeviceType.SPEAKER);
        radio_living_room.setDevice_location(living_room.getUid());
        radio_living_room.setCurrent_state("1");
        radio_living_room.setLaststatechange(System.currentTimeMillis());
        radio_living_room.setCurrent_music("Tony Carreira - Sonhos de Menino");

        outputDeviceRepository.save(radio_living_room);

        OutputDevice dehumidifier = new OutputDevice();
        dehumidifier.setName("Dehumidifier");
        dehumidifier.setDevice_category(OutputDeviceType.DEHUMIDIFER);
        dehumidifier.setDevice_location(living_room.getUid());
        dehumidifier.setCurrent_state("1");
        dehumidifier.setLaststatechange(System.currentTimeMillis());

        outputDeviceRepository.save(dehumidifier);

        OutputDevice ventilator = new OutputDevice();
        ventilator.setName("Ventilator");
        ventilator.setDevice_category(OutputDeviceType.DEHUMIDIFER);
        ventilator.setDevice_location(kitchen.getUid());
        ventilator.setCurrent_state("1");
        ventilator.setLaststatechange(System.currentTimeMillis());

        outputDeviceRepository.save(ventilator);

        kitchen.addDevice(ventilator.getID());
        kitchen.addDevice(ceiling_light.getID());
        living_room.addDevice(lamp_living.getID());
        living_room.addDevice(tv_living.getID());
        living_room.addDevice(radio_living_room.getID());
        couple_bedroom.addDevice(leds_ligits.getID());
        child_bedroom.addDevice(tv_couple_bedr.getID());
    }
}
