abstract class Shape {
    public abstract String toString();
    abstract void draw();
    void print(){
        System.out.println(this.toString());
        draw();
    }

}

class Point extends Shape {
    Point(){}

    public String toString() {
        return "Point";
    }

    void draw(){
        System.out.println('+');
    }
}

abstract class AbstLine extends Shape {
    private int length;

    AbstLine(int length){
        setLength(length);
    }

    int getLength(){
        return length;
    }

    void setLength(int length) {
        this.length = length;
    }

    public String toString() {
        return "AbstLine(length:"+length+")";
    }
}

class HorzLine extends AbstLine {
    HorzLine(int length){
        super(length);
    }

    public String toString() {
        return "HorzLine(length:"+this.getLength()+")";
    }

    void draw() {
        for (int i = 0; i<= getLength();++i){
            System.out.print('-');
        }
        System.out.println();
    }
}

class VertLine extends AbstLine {
    VertLine(int length){
        super(length);
    }

    public String toString() {
        return "VertLine(length:"+this.getLength()+")";
    }

    void draw() {
        for (int i = 0; i<= getLength();++i){
            System.out.println('|');
        }
    }
}

class Rectangle extends Shape {
    private int width, height;

    Rectangle(int width, int height){
        this.width = width; this.height =height;
    }

    public String toString() {
        return "Rectangle(width:"+this.width+",height"+this.height+")";
    }

    void draw() {
        for (int i =0 ; i <= this.height ; ++i){
            for (int j = 0; j<=this.width;++j){
                System.out.print('*');
            }
            System.out.println();
        }
    }
}