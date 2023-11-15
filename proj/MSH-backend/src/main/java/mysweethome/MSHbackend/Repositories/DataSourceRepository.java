package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSourceRepository extends MongoRepository<DataSource, String> {

    // adicionar + cenas conforme for preciso
    
}
