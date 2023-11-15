package mysweethome.MSHbackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mysweethome.MSHbackend.Repositories.DataSourceRepository;
import mysweethome.MSHbackend.Models.DataSource;

@Service
public class DataSourceService {

    @Autowired
    private DataSourceRepository dataSourceRepository;

    public void saveDataSource(DataSource dataSource) {
        dataSourceRepository.save(dataSource);
    }

    
}
