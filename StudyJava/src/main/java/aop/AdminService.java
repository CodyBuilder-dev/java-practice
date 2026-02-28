package aop;

public class AdminService implements UserService {
  @Override
  public void createUser() {
    System.out.println("Admin created!");
  }

  @Override
  public void loginUser() {
    System.out.println("Admin logged in!");
  }

  @Override
  public void logoutUser() {
    System.out.println("Admin logged out!");
  }

  @Override
  public void deleteUser() {
    System.out.println("Admin deleted!");
  }
}
