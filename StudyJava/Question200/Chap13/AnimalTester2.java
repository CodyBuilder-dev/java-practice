class AnimalTester2 {
    public static void main(String[] args){
        Animal2[] a = new Animal2[2];
        a[0] = new Dog("뭉치","치와와");
        a[1] = new Cat("마이클",2);

        for (Animal2 s : a){
            s.introduce();
        }
    }
}