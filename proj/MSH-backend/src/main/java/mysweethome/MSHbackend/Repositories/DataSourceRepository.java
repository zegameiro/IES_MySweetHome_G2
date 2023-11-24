package mysweethome.MSHbackend.Repositories;
import mysweethome.MSHbackend.Models.DataSource;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.LinkedList;



@Repository
public interface DataSourceRepository extends MongoRepository<DataSource, String> {

    // adicionar + cenas conforme for preciso
    
    @Query("{}")
    public LinkedList<DataSource> getAll();

    public DataSource findByID(String id);
}
