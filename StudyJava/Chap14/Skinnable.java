public interface Skinnable {
    // 인터페이스의 멤버 변수
    int BLACK = 0; // 인터페이스 내의 멤버 변수는 자동으로 public static final
    int RED = 1;
    int GREEN = 2;
    int BLUE = 3;
    int YELLOW = 4;
    void changeSkin(int skin);
}

class SkinnableSoftware implements Skinnable {
    int skin;

    // 생성자 2종류 선언 가능
    public SkinnableSoftware(){this.skin = BLACK;}
    public SkinnableSoftware(int skin){this.skin = skin;} 

    public void changeSkin(int skin){ this.skin = skin; }
    public int getSkin() {return this.skin;}
    public String getSkinString() {
        switch(this.skin){
            case BLACK: return "BLACK"; 
            case RED: return "RED"; 
            case GREEN: return "GREEN"; 
            case BLUE: return "BLUE"; 
            case YELLOW: return "YELLOW"; 
        }
        return "";
    }
}