class Animal {
    private String name;

    Animal(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    void bark(){};
}


abstract class AnimalAbstract {
    private String name;

    AnimalAbstract(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    void bark(){};
}

class Dog extends AnimalAbstract {
    private String type;

    Dog(String name,String type) {
        super(name);
        this.type = type;
    }

    void bark() {
        System.out.println("멍멍");
    }
}

class Cat extends AnimalAbstract {
    private int age;

    Cat(String name, int age){
        super(name);
        this.age = age;
    }

    void bark() {
        System.out.println("야옹");
    }
}