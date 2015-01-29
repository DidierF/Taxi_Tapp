package intec.matdiscreta.taxitapp.session;

/**
 * Created by Lou on 1/24/15.
 */
public enum Session {
    FIRST_USER(1, "cooldude58", "Chad", "Man"),
    SECOND_USER(2, "somedude42", "Chuck", "Something"),
    FIRST_TAXI(1, "taximan", "Roberto", "Placeres", true),
    SECOND_TAXI(2, "cabbers", "Andres", "Marques", true);

    public int id;
    public String username, first_name, last_name;
    public boolean isTaxi;

    private Session(int id, String username, String first_name, String last_name) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.isTaxi = false;
    }

    private Session(int id, String username, String first_name, String last_name, boolean isTaxi) {
        this.id = id;
        this.username = username;
        this.first_name = first_name;
        this.last_name = last_name;
        this.isTaxi = isTaxi;
    }
}
