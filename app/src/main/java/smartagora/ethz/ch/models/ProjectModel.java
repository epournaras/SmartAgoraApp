package smartagora.ethz.ch.models;



public class ProjectModel {
    private final String name;
    private final String id; /* 0 -&gt; checkbox disable, 1 -&gt; checkbox enable */
    private boolean selected;

    private boolean autoAssignment;
    //for help screen
    private final String help;

    public ProjectModel(String name, String id, boolean selected, String description){
        this.name = name;
        this.id = id;
        this.selected = selected;
        int j=0;
        int[] index = null;
        if(description.contains("#"))
                index =new int[2];
        for(int i=0;i<description.length();i++) {
            if(description.charAt(i)=='#' && index != null) {
                index[j] = i;
                j++;
            }
        }
        if(index !=null) {
            this.help = description.substring(index[0] + 1, index[1]);
            String str=description.substring(index[1] + 1);
            this.autoAssignment = str.equals("true");
        }else
            help = null;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }

    public boolean getAutoAssignment(){return this.autoAssignment;}

    public boolean isSelected() {
        return this.selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getHelp() {
        return help;
    }

}
