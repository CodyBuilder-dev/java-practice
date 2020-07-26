abstract class AbstTriangle extends Shape {
    private int length;

    AbstTriangle(int length){
        this.length = length;
    }

    public String toString(){
        return "AbstTriangle(length:"+length+")";
    }

    int getLength(){
        return this.length;
    }
}

class TriangleLL extends AbstTriangle {
    TriangleLL(int length) {
        super(length);
    }

    public String toString(){
        return "TriangleLL(length:"+this.getLength()+")";
    }

    void draw(){
        for (int i = 0 ; i < this.getLength() ; ++i){
            for (int j = 0 ; j <= i; ++j)
                System.out.print('*');
            System.out.println();
        }
            
    }
}