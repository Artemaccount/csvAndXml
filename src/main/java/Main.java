import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Employee> employeeList = new ArrayList<>();
//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String csvFileName = "data.csv";
//        String xmlFileName = "data.xml";
        //employeeList = parseCSV(columnMapping, csvFileName);
//        try {
//            employeeList = parseXML(xmlFileName);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        String json = listToJson(employeeList);
//        writeString(json);
        try {
            employeeList = jsonToList("new_data.json");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(employeeList.toString());
    }

    private static List<Employee> jsonToList (String fileName) throws IOException, ParseException {
        List<Employee> employeeList = new ArrayList<>();
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(fileName));
            JSONArray employeeJsonArray = (JSONArray) obj;
            for (Object it : employeeJsonArray) {
                JSONObject employeeJsonObject = (JSONObject) it;
                long id = (Long) employeeJsonObject.get("id");
                String firstName = (String) employeeJsonObject.get("firstName");
                String lastName = (String) employeeJsonObject.get("lastName");
                String country = (String) employeeJsonObject.get("country");
                long age = (Long) employeeJsonObject.get("age");
                Employee employee = new Employee(id, firstName, lastName, country, (int) age);
                employeeList.add(employee);
            }
        return  employeeList;
        }


    private static List<Employee> parseXML(String xmlFileName) {
        List<Employee> employeeList = new ArrayList();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(xmlFileName));
            NodeList nodeList = doc.getElementsByTagName("employee");
            for (int i = 0; i < nodeList.getLength(); i++) {
                employeeList.add(getEmployee(nodeList.item(i)));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return employeeList;
    }

    private static Employee getEmployee(Node node) {
        Employee employee = new Employee();
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            Element element = (Element) node;
            employee.setId(Long.valueOf(getTagValue("id", element)));
            employee.setFirstName(getTagValue("firstName", element));
            employee.setLastName(getTagValue("lastName", element));
            employee.setCountry(getTagValue("country", element));
            employee.setAge(Integer.parseInt(getTagValue("age", element)));
        }
        return employee;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

    private static void writeString(String json) {
        File file = new File("C:\\Users\\riddl\\IdeaProjects\\CSVElseWork\\csvElse\\data.json");
        try (FileWriter fileWriter = new FileWriter(file)) {
            file.createNewFile();
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String listToJson(List<Employee> employeeList) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(employeeList);
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> staff = null;
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);
            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            staff = csv.parse();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
        return staff;
    }
}