package jdbc;


public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public Long getId() { return id; }

    public User(Long id, String firstName, String lastName, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    // Сеттеры (если нужны)
    public void setFirstName(String firstName) { this.firstName = firstName; }
}
