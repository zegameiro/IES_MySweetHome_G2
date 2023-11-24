package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.DataSourceRepository;
import mysweethome.MSHbackend.Models.DataSource;
import java.util.LinkedList;

@Service
public class DataSourceService {

    @Autowired
    private DataSourceRepository dataSourceRepository;

    public void saveDataSource(DataSource dataSource) {
        dataSourceRepository.save(dataSource);
    }

    public DataSource findByID(String id) {
        return dataSourceRepository.findByID(id);
    }

    public LinkedList<DataSource> getAll() {
        return dataSourceRepository.getAll();
    }

}
