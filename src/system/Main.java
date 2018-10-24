package system;

import system.Development;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static StringBuffer report;
    static BufferedReader reader;
    static OutputStream fileOutputStream;
    static SetOfDocuments standart;
    public static void main(String[] args){
        String REPORTFILEADRESS = "D://project//DataAboutDetails.txt";
        String INPUTDATAFILE = "D://project//InputData.txt";
        report = new StringBuffer(200);
        try(FileInputStream fileInputStream = new FileInputStream(INPUTDATAFILE)) {

            reader = new BufferedReader(new InputStreamReader(fileInputStream, Charset.forName("UTF-8")));
            fileOutputStream = new FileOutputStream(REPORTFILEADRESS, true);

            String dataAboutDeveloping = "";
            ArrayList<String> namesOfDetails = new ArrayList<>();
            while((dataAboutDeveloping =reader.readLine())!=null){
                for (String item:parsing(dataAboutDeveloping)) {
                    namesOfDetails.add(item);
                }
            };
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(REPORTFILEADRESS), Charset.forName("UTF-8")));
            while((dataAboutDeveloping = reader.readLine())!=null){
                for (String item:parsing(dataAboutDeveloping)) {
                    namesOfDetails.remove(item);
                }
            };

            Random random = new Random();
            standart = new SetOfDocuments();
            ArrayList<Development> listOfDetail = new ArrayList<>();
            ArrayList<SetOfDocuments>  setOfDocumentsArrayList = new ArrayList<>();
            for(int i=0;i<namesOfDetails.size();i++){
                listOfDetail.add(i,new Development(namesOfDetails.get(i),i, SetOfDocuments.getSubSetOf(standart)));
                setOfDocumentsArrayList.add(listOfDetail.get(i).getSetOfDocuments());
            };

            GUI.createGUI(listOfDetail, setOfDocumentsArrayList, fileOutputStream, report);
        }
        catch (IOException ex){
            System.out.println(ex.getMessage());
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