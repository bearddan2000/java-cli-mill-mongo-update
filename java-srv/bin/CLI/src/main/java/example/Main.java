package example;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates; 
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

   public static void main( String[] args ) {

     // Mongodb initialization parameters.
     int port_no = 27017;
     String auth_user="admin";
     String auth_pwd = "admin";
     String host_name = "db";
     String db_name = "test";
     String encoded_pwd = "";

     try {
         encoded_pwd = URLEncoder.encode(auth_pwd, "UTF-8");
     } catch (UnsupportedEncodingException ex) {
         //log.error(ex);
     }

     // Mongodb connection string.
     String client_url = "mongodb://" + auth_user + ":" + encoded_pwd + "@" + host_name + ":" + port_no + "/" + db_name;
     MongoClientURI uri = new MongoClientURI(client_url);

     // Connecting to the mongodb server using the given client uri.
     MongoClient mongo_client = new MongoClient(uri);

     // Fetching the database from the mongodb.
     MongoDatabase db = mongo_client.getDatabase(db_name);

     MongoCollection<Document> collection = db.getCollection("employees");

  //
  // 4.2 Insert new document
  //

    Document employee = new Document()
                           .append("first_name", "Joe")
                           .append("last_name", "Smith")
                           .append("title", "Java Developer")
                           .append("years_of_service", 3)
                           .append("skills", Arrays.asList("java", "spring", "mongodb"))
                           .append("manager", new Document()
                                                 .append("first_name", "Sally")
                                                 .append("last_name", "Johanson"));

   Document employee2 = new Document()
                          .append("first_name", "Joe")
                          .append("last_name", "Friday")
                          .append("title", "Business Developer")
                          .append("years_of_service", 3)
                          .append("skills", Arrays.asList("social media", "spreadsheet"))
                          .append("manager", new Document()
                                                .append("first_name", "Sally")
                                                .append("last_name", "Johanson"));
    List<Document> list = new ArrayList<Document>();
    list.add(employee);
    list.add(employee2);
    collection.insertMany(list);

    Document query = new Document("last_name", "Smith");
    queryResults(collection, query);

    for (String name : db.listCollectionNames()) {
      System.out.println("[OUTPUT COLLECTION] " + name);
    }
    allResults(collection);
    collection.updateOne(Filters.eq("last_name", "Friday"), Updates.set("last_name", "Zhou"));
    allResults(collection);
   }

   private static void printResults(List results) {
    for (Object o : results) {
      String name = o.toString();
      System.out.println("[OUTPUT RESULT] " + name);
    }
   }
   private static void queryResults(MongoCollection<Document> collection, Document query) {
     List results = new ArrayList<>();
     collection.find(query).into(results);
     printResults(results);
   }
   private static void allResults(MongoCollection<Document> collection) {
     List results = new ArrayList<>();
     collection.find().into(results);
     printResults(results);
   }
}
