package smartagora.ethz.ch.models;

public class SensorModel {
    private final String Name;

    public SensorModel(String name){
        this.Name = name;
    }

    public String getName(){
        return Name;
    }

}
