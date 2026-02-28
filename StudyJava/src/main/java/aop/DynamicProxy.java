package aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxy {

  public static void main(String[] args) {
    // UserService 인터페이스에 프록시 객체 인스턴스를 런타임에 동적으로 등록하는 방법.
    UserService adminService = (UserService) Proxy.newProxyInstance(
        UserService.class.getClassLoader() // 프록시를 만들 ClassLoader
        ,new Class[]{UserService.class} // 프록시를 만들 인터페이스
        ,new InvocationHandler() { //invoke handler, 프록시의 내용. 직접 작성한 구현체
          final UserService userService = new AdminService();

          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            // 아무런 동작도 하지않고, 단순히 실제 객체에게 위임하는 프록시 코드.
            // Object invoke = method.invoke(...)등으로 리플렉션 조작가능.
            // method.invoke(메서드를 호출할 객체 , 메서드에 전달할 파라메타)
            // 여기에 코드를 작성하면 객체의 모든 메서드가 동일한 영향을 받는다.
            System.out.println("Now admin user is executing the method:");
            Object returnValue = method.invoke(userService, args);
            System.out.println("Now admin user is done executing the method:");
            return returnValue;
          }
        });

    adminService.createUser();
    adminService.deleteUser();

    UserService newbieService = new NewbieService();
    newbieService.loginUser();
  }
}
