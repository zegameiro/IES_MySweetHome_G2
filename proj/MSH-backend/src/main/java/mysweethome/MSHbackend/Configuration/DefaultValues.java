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

        System.out.println("Inserting default values into the Database");
        // Insert default values
        OutputDevice luz_cozinha = new OutputDevice();
        luz_cozinha.setName("Luz da Cozinha");
        luz_cozinha.setDevice_category(OutputDeviceType.LIGHT);
        luz_cozinha.setDevice_location("Cozinha");
        luz_cozinha.setCurrent_state("OFF");
        luz_cozinha.setLaststatechange(System.currentTimeMillis());
        luz_cozinha.setColor("white");

        outputDeviceRepository.save(luz_cozinha);

        OutputDevice candeeiro_sala = new OutputDevice();
        candeeiro_sala.setName("Candeeiro da Sala");
        candeeiro_sala.setDevice_category(OutputDeviceType.LIGHT);
        candeeiro_sala.setDevice_location("Sala");
        candeeiro_sala.setCurrent_state("ON");
        candeeiro_sala.setLaststatechange(System.currentTimeMillis());
        candeeiro_sala.setColor("white");

        outputDeviceRepository.save(candeeiro_sala);

        OutputDevice rgb_quarto = new OutputDevice();
        rgb_quarto.setName("Luz RGB do Quarto");
        rgb_quarto.setDevice_category(OutputDeviceType.LIGHT);
        rgb_quarto.setDevice_location("Quarto");
        rgb_quarto.setCurrent_state("ON");
        rgb_quarto.setLaststatechange(System.currentTimeMillis());
        rgb_quarto.setColor("red");

        outputDeviceRepository.save(rgb_quarto);

        OutputDevice ar_condicionado = new OutputDevice();
        ar_condicionado.setName("Ar Condicionado Sala");
        ar_condicionado.setDevice_category(OutputDeviceType.AIR_CONDITIONER);
        ar_condicionado.setDevice_location("Quarto");
        ar_condicionado.setCurrent_state("ON");
        ar_condicionado.setLaststatechange(System.currentTimeMillis());
        ar_condicionado.setTemperature(25);

        outputDeviceRepository.save(ar_condicionado);

        OutputDevice aquecedor_sotao = new OutputDevice();
        aquecedor_sotao.setName("Aquecedor do Sótão");
        aquecedor_sotao.setDevice_category(OutputDeviceType.AIR_CONDITIONER);
        aquecedor_sotao.setDevice_location("Sótão");
        aquecedor_sotao.setCurrent_state("OFF");
        aquecedor_sotao.setLaststatechange(System.currentTimeMillis());
        aquecedor_sotao.setTemperature(20);

        outputDeviceRepository.save(aquecedor_sotao);

        OutputDevice tv_sala = new OutputDevice();
        tv_sala.setName("Televisão da Sala");
        tv_sala.setDevice_category(OutputDeviceType.TELEVISION);
        tv_sala.setDevice_location("Sala");
        tv_sala.setCurrent_state("ON");
        tv_sala.setLaststatechange(System.currentTimeMillis());
        tv_sala.setCurrent_channel("RTP1");

        outputDeviceRepository.save(tv_sala);

        OutputDevice tv_quarto = new OutputDevice();
        tv_quarto.setName("Televisão do Quarto");
        tv_quarto.setDevice_category(OutputDeviceType.TELEVISION);
        tv_quarto.setDevice_location("Quarto");
        tv_quarto.setCurrent_state("ON");
        tv_quarto.setLaststatechange(System.currentTimeMillis());
        tv_quarto.setCurrent_channel("TVI");

        outputDeviceRepository.save(tv_quarto);

        OutputDevice radio_sala = new OutputDevice();
        radio_sala.setName("Rádio da Sala");
        radio_sala.setDevice_category(OutputDeviceType.SPEAKER);
        radio_sala.setDevice_location("Sala");
        radio_sala.setCurrent_state("ON");
        radio_sala.setLaststatechange(System.currentTimeMillis());
        radio_sala.setCurrent_music("Tony Carreira - Sonhos de Menino");

        outputDeviceRepository.save(radio_sala);

        OutputDevice desumidificador = new OutputDevice();
        desumidificador.setName("Desumidificador");
        desumidificador.setDevice_category(OutputDeviceType.DEHUMIDIFER);
        desumidificador.setDevice_location("Sala");
        desumidificador.setCurrent_state("ON");
        desumidificador.setLaststatechange(System.currentTimeMillis());

        outputDeviceRepository.save(desumidificador);

        OutputDevice exaustor = new OutputDevice();
        exaustor.setName("Exaustor");
        exaustor.setDevice_category(OutputDeviceType.DEHUMIDIFER);
        exaustor.setDevice_location("Cozinha");
        exaustor.setCurrent_state("ON");
        exaustor.setLaststatechange(System.currentTimeMillis());

        outputDeviceRepository.save(exaustor);

        System.out.println("Inserted default output devices sucessfully! ");

        Room sala = new Room();
        sala.setName("Sala");
        sala.setFloorNumber(0);

        Room quarto_putos = new Room();
        quarto_putos.setName("Quarto das Crianças");
        quarto_putos.setFloorNumber(1);

        Room quarto_casal = new Room();
        quarto_casal.setName("Quarto do Casal");
        quarto_casal.setFloorNumber(1);

        Room casa_de_banho_1 = new Room();
        casa_de_banho_1.setName("Casa de Banho 1");
        casa_de_banho_1.setFloorNumber(0);

        Room casa_de_banho_quartos = new Room();
        casa_de_banho_quartos.setName("Casa de Banho dos Quartos");
        casa_de_banho_quartos.setFloorNumber(1);

        Room cozinha = new Room();
        cozinha.setName("Cozinha");
        cozinha.setFloorNumber(0);

        cozinha.addDevice(exaustor.getID());
        cozinha.addDevice(luz_cozinha.getID());
        sala.addDevice(candeeiro_sala.getID());
        sala.addDevice(tv_sala.getID());
        sala.addDevice(radio_sala.getID());
        quarto_casal.addDevice(rgb_quarto.getID());
        quarto_casal.addDevice(tv_quarto.getID());

        roomRepository.save(cozinha);
        roomRepository.save(casa_de_banho_quartos);
        roomRepository.save(casa_de_banho_1);
        roomRepository.save(quarto_putos);
        roomRepository.save(quarto_casal);
        roomRepository.save(sala);

        System.out.println("Inserted default rooms sucessfully! ");

    }
}
