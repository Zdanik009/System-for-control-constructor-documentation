package system;

public class Development {
    private SetOfDocuments setOfDocuments;
    private String name;
    private int index;

    public  Development(String name, int index,SetOfDocuments setOfDocuments){
        this.name = name;
        this.index = index;
        this.setOfDocuments = setOfDocuments;
        setOfDocuments.setNameOfSet(name);
    };

    public void addDocument(Character nameOfDoc, Boolean state){
        this.setOfDocuments.addDocument(nameOfDoc,state);
    };

    public void setSetOfDocuments(SetOfDocuments setOfDocuments){
        this.setOfDocuments = setOfDocuments;
    }

    public SetOfDocuments getSetOfDocuments(){ return this.setOfDocuments; }

    public void setStateofDoc(String key, Boolean state){
        this.setOfDocuments.setStateofDoc(key, state);
    };

    public boolean getStateOfDocument(String key){
        return this.setOfDocuments.getStateOfDocument(key);
    };

    public Character getNameOfDocument(int index) {
        return this.setOfDocuments.getNameOfDocument(index);
    };

    public int getCountOfDocuments() { return this.setOfDocuments.getCountOfDocuments(); };

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