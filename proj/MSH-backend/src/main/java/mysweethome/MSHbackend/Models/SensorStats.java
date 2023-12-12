package mysweethome.MSHbackend.Models;

import java.util.ArrayList;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SensorStats {

    private int category;
    private String unit;
    private ArrayList<String> values;

    
}
