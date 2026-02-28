package aop;

public class NewbieService implements UserService{
  @Override
  public void createUser() {
    System.out.println("Newbie created!");
  }
  @Override
  public void loginUser() {
    System.out.println("Newbie logged in!");
  }
  @Override
  public void logoutUser() {
    System.out.println("Newbie logged out!");
  }
  @Override
  public void deleteUser() {
    System.out.println("Newbie deleted!");
  }
}
