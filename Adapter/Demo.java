package AdapterPattern;

public class Demo {
    public static void main(String[] args) {

        Student college = new CollegeStudent("sss","sss","sss");
        System.out.println(college.getEmail());

        // For this we created one driver class to get and set the attribute
        Student school = new Adapter(new SchoolStudent("TEST","SSDSD","DSDSD"));
        System.out.println(school.getEmail());

    }
}
