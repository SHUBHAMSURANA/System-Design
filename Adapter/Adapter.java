package AdapterPattern;

// Rember the adapter class will implement the current interface and try to get SchoolStudent
public class Adapter implements  Student{
    SchoolStudent schoolStudent;

    public Adapter(SchoolStudent schoolStudent) {
        this.schoolStudent = schoolStudent;
    }

    public String getName() {
        return this.schoolStudent.getName();
    }


    public String getSurnameName() {
        return this.schoolStudent.getSurnameName();
    }


    public String getEmail() {
        return this.schoolStudent.getEmail();
    }

}
