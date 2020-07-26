class AnimalTester1 {
    public static void main(String[] args){
        
        Animal animal = new Animal("동물"); 
        animal.bark();
        //컴파일 에러 발생. 추상클래스는 인스턴스 생성 불가
        //AnimalAbstract animalAbstract = new AnimalAbstract(); 
        
        Animal[] a = new Animal[2];
        a[0] = new Dog("뭉치","치와와");
        a[1] = new Cat("마이클",2);

        for (Animal s : a){
            System.out.println(s.getName());
            s.bark();
        }
    }
}