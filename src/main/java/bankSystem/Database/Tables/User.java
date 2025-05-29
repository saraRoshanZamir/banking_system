//package bankSystem.Database.Tables;
//
//import jakarta.persistence.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Table(name = "users")
//public class User {
//    @Id
//    @Column(name = "id")
//    private int id;
//
//    @Column(name = "user_id", unique = true)
//    private int userId;
//
//    @Column(name = "first_name")
//    private String firstName;
//
//    @Column(name = "last_name")
//    private String lastName;
//
//    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
//    @JoinColumn(name= "id")
//    private List<Account> Accounts = new ArrayList<>();
//
//    public User() {
//    }
//
//    public String getFirstName() {
//        return firstName;
//    }
//    public String getLastName() {
//        return lastName;
//    }
//
//    public List<Account> getAccounts() {
//        return Accounts;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" +
//                "id=" + id +
//                ", userId=" + userId +
//                ", firstName='" + firstName + '\'' +
//                ", lastName='" + lastName + '\'' +
//                ", Accounts=" + Accounts +
//                '}';
//    }
//}
