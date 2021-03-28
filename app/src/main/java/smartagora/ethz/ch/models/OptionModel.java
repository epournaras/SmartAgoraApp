package smartagora.ethz.ch.models;

/**
 * Options model that store options in answer while at survey.
 */

public class OptionModel {
    private final String Name;

    public OptionModel(){
        this(null);
    }

    public OptionModel(String name){
        this.Name = name;
    }

    public String getName(){
        return Name;
    }
}
