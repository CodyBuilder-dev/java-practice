public interface Plane2D {
    int getArea();
}

class Rectangle extends Shape implements Plane2D {
    private int width; //너비
    private int height; //높이

    public Rectangle(int width, int height){
        this.width = width; this.height = height;
    }

    public String toString() {
        return "Rectangle(width:"+this.width+",height:"+this.height+")";
    }

    public void draw(){
        for (int i = 1; i<= this.height ; ++i){
            for (int j = 1; j<= this.width ; ++j){
                System.out.print('+');
                System.out.println();
            }
        }
    }

    public int getArea() { return width * height;}
}


class Parallelogram extends Shape implements Plane2D {
    private int width; //너비
    private int height; //높이

    public Parallelogram(int width, int height){
        this.width = width; this.height = height;
    }

    public String toString() {
        return "Parallelogram(width:"+this.width+",height:"+this.height+")";
    }

    public void draw(){
        for (int i = 1; i<= this.height ; ++i){
            for (int j = 1 ; j <= this.height-i ; ++j) System.out.print(' ');
            for (int j = 1 ; j <= this.width ; ++j) System.out.print('#');
            System.out.println();
            
        }
    }

    public int getArea() { return width * height;}
}
