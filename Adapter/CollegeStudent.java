package AdapterPattern;

public class CollegeStudent implements Student
{
    private String name;
    private String surname;
    private String email;

    CollegeStudent (String name,String surname, String email) {
        this.name = name;
        this.email= email;
        this.surname=surname;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSurnameName() {
        return this.surname;
    }

    @Override
    public String getEmail() {
        return this.email;
    }
}
