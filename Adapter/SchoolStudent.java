package AdapterPattern;

// Assuem this class is some legacy code and we want to use in demo class , so we build an adapter class to make the link.
// Observe we are not implementing the interafce as well
public class SchoolStudent {
    private String name;
    private String surname;
    private String email;

    SchoolStudent (String name,String surname, String email) {
        this.name = name;
        this.email= email;
        this.surname=surname;
    }


    public String getName() {
        return this.name;
    }


    public String getSurnameName() {
        return this.surname;
    }

    public String getEmail() {
        return this.email;
    }
}
