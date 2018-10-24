package system;

import java.util.*;

public class SetOfDocuments {
    private Map<Character,Boolean> documents;
    private String nameOfSet;
    private int AMOUNT_OF_DOCUMENTS = 10;

    protected SetOfDocuments(){
        this.documents = new HashMap<Character, Boolean>(AMOUNT_OF_DOCUMENTS);
        char A = 'A';
        for(int i=0;i<AMOUNT_OF_DOCUMENTS;i++,A++)
            documents.put(A,false);
    };

    public SetOfDocuments(String nameOfSet, ArrayList<Character> listOfAddedDocs) {
        this.nameOfSet = nameOfSet;
        this.documents = new HashMap<Character, Boolean>();
        for (Character document:listOfAddedDocs
        ) {
            this.documents.put(document,false);
        }
    };

    public SetOfDocuments(int amount){
        this.documents = new HashMap<Character, Boolean>(amount);
    };

    public void addDocument(Character nameOfDoc, Boolean state){
        this.documents.put(nameOfDoc,state);
    };

    public void setStateofDoc(String key, Boolean state){
        this.documents.put(key.charAt(0), state);
    };

    public boolean getStateOfDocument(String key){
        return this.documents.get(key.charAt(0)).booleanValue();
    };

    public Character getNameOfDocument(int index) {
        Character[] nameOfDoc = new Character[26];
        Set<Character> characterSet = this.documents.keySet();
        characterSet.toArray(nameOfDoc);
        return nameOfDoc[index];
    };

    public int getCountOfDocuments() { return this.documents.size(); };

    public static SetOfDocuments getSubSetOf(SetOfDocuments standart) {
        Random random = new Random();
        SetOfDocuments subset = new SetOfDocuments(random.nextInt(9)+1);
        for(int i=0;i<random.nextInt(4)+1;i++)
            subset.documents.put(standart.getNameOfDocument(random.nextInt(standart.getCountOfDocuments())),random.nextBoolean());
        return subset;
    };

    public void setNameOfSet(String nameOfSet){
        this.nameOfSet = nameOfSet;
    }

    public String getNameOfSet(){
        return this.nameOfSet;
    }
}
