package aop;

import me.codybuilder.annotationProcessor.CustomGetter;

@CustomGetter
public class CustomUser {

  private String name;

  public int age;
  private String address;

  public CustomUser(String name, int age, String address) {
    this.name = name;
    this.age = age;
    this.address = address;
  }

  public static void main(String[] args) {
    CustomUser user = new CustomUser("Tom",20, "Seoul");
    System.out.println(user.getName());
  }
}
