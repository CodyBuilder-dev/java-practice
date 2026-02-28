package designPattern;

public class DecoratorPattern {
  public static void main(String[] args) {
    Client client = new Client();
    client.call();
  }
}

// 원본 객체와 장식된 객체 모두를 묶는 인터페이스
interface IComponent {
  void operation();
}

// 장식될 원본 객체
class ConcreteComponent implements IComponent {
  public void operation() {
    System.out.println("원본 객체의 메서드가 실행됩니다.");
  }
}

// 장식자 추상 클래스
abstract class Decorator implements IComponent {
  IComponent wrappee; // 원본 객체를 composition

  Decorator(IComponent component) {
    this.wrappee = component;
  }

  public void operation() {
    wrappee.operation(); // 위임
  }
}

// 장식자 클래스
class ComponentDecorator1 extends Decorator {

  ComponentDecorator1(IComponent component) {
    super(component);
  }

  public void operation() {
    extraOperation(); // 장식 클래스만의 메소드를 실행한다.
    super.operation(); // 원본 객체를 상위 클래스의 위임을 통해 실행하고
  }

  void extraOperation() {
    System.out.println("이 로그는 메서드 앞에 수행됩니다.");
  }
}

class ComponentDecorator2 extends Decorator {

  ComponentDecorator2(IComponent component) {
    super(component);
  }

  public void operation() {
    super.operation(); // 원본 객체를 상위 클래스의 위임을 통해 실행하고
    extraOperation(); // 장식 클래스만의 메소드를 실행한다.
  }

  void extraOperation() {
    System.out.println("이 로그는 메서트 뒤에 수행됩니다.");
  }
}

// Decorator를 호출하는 클라이언트
class Client {
  public void call() {
    IComponent component = new ConcreteComponent();
    IComponent decorator1 = new ComponentDecorator1(component);
    decorator1.operation();

    IComponent decorator2 = new ComponentDecorator2(component);
    decorator2.operation();
  }
}