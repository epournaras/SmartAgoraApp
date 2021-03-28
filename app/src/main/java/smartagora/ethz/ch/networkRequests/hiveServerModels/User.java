package smartagora.ethz.ch.networkRequests.hiveServerModels;

import com.google.gson.annotations.SerializedName;

/**
 * User Class is a JSON representation of User which is created in Hive Server.
 * User Class includes different attributes to define the each User.
 */
public class User {

    @SerializedName("Id")
    private String id;

    @SuppressWarnings("FieldCanBeLocal")
    @SerializedName("Name")
    private String name;

    @SuppressWarnings("FieldCanBeLocal")
    @SerializedName("Email")
    private String email;

    @SerializedName("Project")
    private String project;

    @SerializedName("ExternalId")
    private String externalId;


    //All setter and getters of above variables

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

}
