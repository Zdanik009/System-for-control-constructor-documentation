package system;

public class Development {
    private boolean Document1 = false;
    private boolean Document2 = false;
    private boolean Document3 = false;
    private boolean Document4 = false;
    private String name;
    private int index;

    public  Development(String name, int index){
        this.name = name;
        this.index = index;
    };

    public void setStateOfDocuments(boolean document1, boolean document2, boolean document3, boolean document4) {
        Document1 = document1;
        Document2 = document2;
        Document3 = document3;
        Document4 = document4;
    };

    public boolean getStateOfDoc1(){ return Document1; };

    public boolean getStateOfDoc2(){ return Document2; };

    public boolean getStateOfDoc3(){ return Document3; };

    public boolean getStateOfDoc4(){ return Document4; };

    public void setName(String name) {
        name = name;
    };

    public String getName() {
        return name;
    }

    public int getIndexOfDetail(String name){
        return index;
    };
}
