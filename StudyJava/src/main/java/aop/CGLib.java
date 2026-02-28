package aop;


import java.lang.reflect.Method;
import net.sf.cglib.beans.BeanGenerator;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.FixedValue;
import net.sf.cglib.proxy.MethodInterceptor;


public class CGLib {

  public static void main(String[] args) throws Exception {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(PersonService.class);
    enhancer.setCallback((FixedValue) () -> "Hello Tom!");
    PersonService proxy = (PersonService) enhancer.create();

    System.out.println( proxy.sayHello(null));

    main2();
    main3();
  }

  public static void main2() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(PersonService.class);
    enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
      if (method.getDeclaringClass() != Object.class && method.getReturnType() == String.class) {
        return "Hello Tom!";
      } else {
        return proxy.invokeSuper(obj, args);
      }
    });

    PersonService proxy = (PersonService) enhancer.create();

    System.out.println( proxy.sayHello(null));
    int lengthOfName = proxy.lengthOfName("Mary");

    System.out.println(lengthOfName);
  }

  public static void main3() throws Exception {
    BeanGenerator beanGenerator = new BeanGenerator();

    beanGenerator.addProperty("name", String.class);
    Object myBean = beanGenerator.create();
    Method setter = myBean.getClass().getMethod("setName", String.class);
    setter.invoke(myBean, "some string value set by a cglib");

    Method getter = myBean.getClass().getMethod("getName");
    System.out.println(getter.invoke(myBean));
  }
}
