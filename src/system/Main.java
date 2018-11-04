package system;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    static BufferedReader reader;
    static SetOfDocuments standart;
    static Path REPORTFILEADRESS;
    static Path INPUTDATAFILE;

    public static void main(String[] args){
        REPORTFILEADRESS = Paths.get("D://project//DataAboutDetails.txt");
        INPUTDATAFILE = Paths.get("D://project//InputData.txt");

        try(FileInputStream fileInputData = new FileInputStream(INPUTDATAFILE.toFile())) {

            reader = new BufferedReader(new InputStreamReader(fileInputData, Charset.forName("UTF-8")));
            FileOutputStream reportFile = new FileOutputStream(REPORTFILEADRESS.toFile(), true);

            String dataAboutDeveloping = "";
            ArrayList<String> namesOfDetails = new ArrayList<>();
            while((dataAboutDeveloping =reader.readLine())!=null){
                for (String item:parsing(dataAboutDeveloping)) {
                    namesOfDetails.add(item);
                }
            };
            reader.close();

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(REPORTFILEADRESS.toFile()), Charset.forName("UTF-8")));
            while((dataAboutDeveloping = reader.readLine())!=null){
                for (String item:parsing(dataAboutDeveloping)) {
                    namesOfDetails.remove(item);
                }
            };
            reader.close();

            standart = new SetOfDocuments();
            ArrayList<Development> listOfDetail = new ArrayList<>();
            ArrayList<SetOfDocuments>  setOfDocumentsArrayList = new ArrayList<>();
            for(int i=0;i<namesOfDetails.size();i++){
                listOfDetail.add(i,new Development(namesOfDetails.get(i),i, SetOfDocuments.getSubSetOf(standart)));
                setOfDocumentsArrayList.add(listOfDetail.get(i).getSetOfDocuments());
            };

            GUI.createGUI(listOfDetail, setOfDocumentsArrayList, REPORTFILEADRESS);
        }
        catch (IOException ex){
            ex.printStackTrace();
        };
    }
    public static ArrayList<String> parsing(String data){
        Pattern p = Pattern.compile("(\\x22)[a-z]+");
        Matcher m = p.matcher(data);
        ArrayList<String> list = new ArrayList<>();
        while(m.find()) {
            list.add(data.substring(m.start()+1,m.end()));
        };
        return list;
    }
}