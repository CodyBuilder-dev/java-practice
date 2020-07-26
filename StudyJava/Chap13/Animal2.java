class Animal2 {
    private String name;

    Animal2(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    void bark(){};
    //String toString(){ return ""; };
    public String toString() { return ""; };
    void introduce(){};
}

class Dog extends Animal2 {
    private String type;

    Dog(String name, String type){
        super(name);
        this.type = type;
    }

    void bark(){
        System.out.println("멍멍!");
    }

    public String toString(){
        return String.format("%s의 %s",this.type,this.getName());
    }

    void introduce(){
        System.out.print(this.toString()+"이다.");
        this.bark();
    }
}

class Cat extends Animal2 {
    private int age;

    Cat(String name, int age){
        super(name);
        this.age = age;
    }

    void bark(){
        System.out.println("야옹!");
    }

    public String toString(){
        return String.format("%s살의 %s",this.age,this.getName());
    }

    void introduce(){
        System.out.print(this.toString()+"이다.");
        this.bark();
    }
}