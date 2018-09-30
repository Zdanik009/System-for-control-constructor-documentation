package system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Development {
    private Map<Character,Boolean> documents = new HashMap<Character, Boolean>();
    //private ArrayList<Boolean> documents = new ArrayList<Boolean>();
    private String name;
    private int index;

    public  Development(String name, int index){
        this.name = name;
        this.index = index;
        //this.documents.get(index).put(nameOfDoc,state);
        //this.documents.add(0,false);
    };

    public void addDocument(Character nameOfDoc, Boolean state){
        this.documents.put(nameOfDoc,state);
        //this.documents.add(index,(Boolean) state);
    };

    /*public void setStateOfDocuments(ArrayList<Boolean> documents) {
        this.documents.addAll(documents);
    };*/

    public void setStateofDoc(String key, Boolean state){
        //String nameOfDoc = this.documents.get(index).;
        this.documents.put(key.charAt(0), state);
        //this.documents.set(index,state);
    };

    public boolean getStateOfDocument(String key){
        return this.documents.get(key.charAt(0)).booleanValue();
    };

    //public boolean getStateOfDocument(String key) { return this.documents.get(key).booleanValue(); };

    public Character getNameOfDocument(int index) {
        Character[] nameOfDoc = new Character[26];
        Set<Character> characterSet = this.documents.keySet();
        characterSet.toArray(nameOfDoc);
        return nameOfDoc[index];
    };

    public int getCountOfDocuments() { return this.documents.size(); };

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