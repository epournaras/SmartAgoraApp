package smartagora.ethz.ch.models;

/**
 * Slide menu items model to store the menu items information.
 */

public class SliderMenuModel {

    private final String iconName; // menu icon name

    public SliderMenuModel(String iconName){
        this.iconName = iconName;
    }

    /**
     * setter and getter for above variables
     */
    public String getIconName() {
        return iconName;
    }

}
