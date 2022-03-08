/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc_project;
import java.sql.*;
import java.util.Scanner;
class CheckInput 
{
      /**
     * Checks if the inputted value is an integer.
     * @return the valid input.
     */
    public static int getInt() 
    {
        Scanner in = new Scanner( System.in );
        int input = 0;
        boolean valid = false;
        while( !valid )
        {

            if( in.hasNextInt() ) 
            {
                input = in.nextInt();
                valid = true;
            } 
            else 
            {  
                in.next(); //clear invalid string
                System.out.println( "Invalid Input. Please must be Integer." );
            }
        }
        return input;
    }
    /**
     * Checks if the inputted value is an integer and 
     * within the specified range (ex: 1-10)
     * @param low lower bound of the range.
     * @param high upper bound of the range.
     * @return the valid input.
     */
    public static int getIntRange( int low, int high )
    {
        Scanner in = new Scanner( System.in );
        int input = 0;
        boolean valid = false;
        while( !valid ) 
        {
            if( in.hasNextInt() ) 
            {
                input = in.nextInt();
                if( input <= high && input >= low )
                {
                    valid = true;
                } else 
                {
                    System.out.println( "Your number is out of range. select between "+low+" - "+high );
                }
            } else 
            {
                in.next(); //clear invalid string
                System.out.println( "Your number is out of range. select between "+low+" - "+high );
            }
        }
        return input;
    }
    
    /**
    * Takes in a string from the user.
    * @return the inputted String.
    */
    public static String getString() 
    {
        Scanner in = new Scanner( System.in );
        String input = in.nextLine();
        return input;
    }

    /**
     * Takes in a yes/no from the user.
     * @return true if yes, false if no.
     */
    public static boolean getYesNo()
    {
        boolean valid = false;
        while( !valid ) 
        {
            String s = getString();
            if( s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y") )
            {
                return true;
            } 
            else if( s.equalsIgnoreCase("no") || s.equalsIgnoreCase("n") ) 
            {
                return false;
            }
            else 
            {
                System.out.println( "Invalid Input." );
            }
        }
        return false;
    }
}
public class JDBC_Project 
{
    static final String displayFormat="%-5s%-15s%-15s%-15s\n";
    public static int menu(){
        int choice=0;
        System.out.println("1. List all writing groups \n"
                + "2. List all the data for a group specified by user\n"
                + "3. List all Publishers\n"
                + "4. List all data for a publisher specified by user\n"
                + "5. List all book titles\n"
                + "6. List all the data for a single book specified by the user\n"
                + "7. Insert a new book\n"
                + "8. Insert or Modify a new publisher\n"
                + "9. Remove a single book specified by user\n"
                + "0. Exit Program");
        Scanner in=new Scanner(System.in);
        /*
        try {
            choice = in.nextInt();
            if (choice < 0 || choice > 9) {
                System.out.println("ERROR: Value was not within specified bounds. Returning to menu.");
            }
            
        } catch (java.util.InputMismatchException e) {
            System.out.println("ERROR: Value must be a number. Returning to menu.");
            choice = menu();
        }
        return choice;
        */
        choice = CheckInput.getIntRange(0,9);
        return choice;
    }
    //  Database credentials
    static String USER;
    static String PASS;
    static String DBNAME;

// JDBC driver name and database URL
    static final String JDBC_DRIVER = "org.apache.derby.jdbc.ClientDriver";
    static String DB_URL = "jdbc:derby://localhost:1527/";
//            + "testdb;user=";
    public static String dispNull (String input) {
        //because of short circuiting, if it's null, it never checks the length.
        if (input == null || input.length() == 0)
            return "N/A";
        else
            return input;
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //Prompt the user for the database name, and the credentials.
        //If your database has no credentials, you can update this code to
        //remove that from the connection string.
        Scanner in = new Scanner(System.in);
        //System.out.print("Name of the database (not the user account): ");
        //DBNAME = in.nextLine();
        DBNAME="JDBC_Project";
        //System.out.print("Database user name: ");
        //USER = in.nextLine();
        //USER="app";
        //System.out.print("Database password: ");
        //PASS = in.nextLine();
        //PASS="pass";
        //Constructing the database URL connection string
        DB_URL = DB_URL + DBNAME;// + ";user="+ USER + ";password=" + PASS;
                Connection conn = null; //initialize the connection
                Statement stmt = null;  //initialize the statement that we're using
                PreparedStatement preparedStatement=null;

                        //STEP 2: Register JDBC driver
                Class.forName("org.apache.derby.jdbc.ClientDriver");

            //STEP 3: Open a connection
                System.out.println("Connecting to database...");
                conn = DriverManager.getConnection(DB_URL);
           
                ResultSet rs=null;
            boolean searching=true;
            while(searching){
       
                int choice=menu();
                switch (choice){
                    case 0: { System.exit(0); }
                    case 1: {
                        System.out.println("Creating statement...");
                    stmt = conn.createStatement();
                    String sql;
                    sql = "SELECT groupName, headwriter, yearformed, subject FROM writinggroups";
                    rs = stmt.executeQuery(sql);
                    String displayFormat1 = "%-25s%-25s%-25s%-25s\n";
                    System.out.printf(displayFormat1, "Group Name", "Head Writer", "Year Formed", "Subject");
                    while (rs.next()) {
                        //Retrieve by column name
                        String groupname = rs.getString("groupname");
                        String headwriter = rs.getString("headwriter");
                        String yearformed = rs.getString("yearformed");
                        String subject = rs.getString("subject");
                        //Display values
                        

                        System.out.printf(displayFormat1,
                                dispNull(groupname), dispNull(headwriter), dispNull(yearformed), dispNull(subject));
                    }
                    break;
                    }//done
                    //to do= input validation and action messages.

                   // 2. List all the data for a group specified by user
                   // 2. List all the data for a group specified by user
                   case 2: 
                   {
                        System.out.print("Enter the Writing Group name: ");
                        String input = in.nextLine().toUpperCase();
                        
                        String checkGroupSQL = "Select groupName FROM WritingGroups";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(checkGroupSQL);
                        boolean match = false;

                        // Check to see if groupName matches any existing groupNames
                        while (!match) 
                        {
                            try 
                            {
                                // iterate through existing names and check for match
                                // if no match is found exception is thrown and process repeats
                                // if match found while is broken and proceed forward
                                while (rs.next()) 
                                {
                                    String group = rs.getString("groupName").toUpperCase();
                                    if(group.equals(input))
                                    {
                                        match = true;
                                    }
                                    if (match) 
                                    {
                                        break;
                                    }
                                }
                                if(!match)
                                {
                                    throw new Exception();
                                }
                            } 
                            catch (Exception e) 
                            {
                                System.out.println("Error: Group not found.");
                                System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                                int exitchoice=CheckInput.getIntRange(1,2);
                                if(exitchoice==2)break;
                                System.out.println("Enter valid group:");
                                input = in.nextLine().toUpperCase();

                                checkGroupSQL = "Select groupName FROM WritingGroups";
                                stmt = conn.createStatement();
                                rs = stmt.executeQuery(checkGroupSQL);
                            }
                        }


                        String searchbygroupname = "SELECT groupName, headwriter, yearformed, subject, bookTitle, yearPublished, numberPages, publisherName, publisherAddress, publisherPhone, publisherEmail FROM WritingGroups INNER JOIN (Books NATURAL JOIN Publishers) USING(GROUPNAME) WHERE UPPER(GROUPNAME) = ?";
                        preparedStatement = conn.prepareStatement(searchbygroupname);
                        preparedStatement.setString(1, input);
                        rs = preparedStatement.executeQuery();

                        String case2Format = "%-25s%-25s%-20s%-10s%-25s%-20s%-20s%-25S%-65s%-20s%-40s\n";
                        System.out.printf(case2Format, "Writing Group", "Head Writer", "Year Formed", "Subject", "Book Title",
                                          "Year Published", "Number of Pages", "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");

                        while (rs.next()) 
                        {
                            String groupname = rs.getString("groupname");
                            String headwriter = rs.getString("headwriter");
                            String yearformed = String.valueOf(rs.getInt("yearformed"));
                            String subject = rs.getString("subject");
                            
                            String bookTitle = rs.getString("bookTitle");
                            int yearPublished = rs.getInt("yearPublished");
                            int numberPages = rs.getInt("numberPages");
                            
                            String publisherName = rs.getString("publisherName");
                            String publisherAddress = rs.getString("publisherAddress");
                            String publisherPhone = rs.getString("publisherPhone");
                            String publisherEmail = rs.getString("publisherEmail");
                            
                            
                            
                            System.out.printf(case2Format,
                                    dispNull(groupname), dispNull(headwriter), dispNull(yearformed), dispNull(subject),
                                    dispNull(bookTitle), dispNull(Integer.toString(yearPublished)), dispNull(Integer.toString(numberPages)), 
                                    dispNull(publisherName), dispNull(publisherAddress), dispNull(publisherPhone), dispNull(publisherEmail));
                        }
                        break;
                   }//List all the data for a group specified by user//prepared statement
                    //to do= input validation and action messages.

                  case 3: {
                        System.out.println("Creating statement...");
                        stmt = conn.createStatement();
                        String sql;
                        sql = "SELECT publisherName, publisherAddress, publisherPhone, publisherEmail FROM publishers";
                        rs = stmt.executeQuery(sql);

                        String displayFormat1="%-25s%-70s%-20s%-40s\n";
                        System.out.printf(displayFormat1, "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");
                        while (rs.next()) {
                                //Retrieve by column name
                                String publishername = rs.getString("publishername");
                                String publisheraddress = rs.getString("publisheraddress");
                                String publisherphone = rs.getString("publisherphone");
                                String publisheremail = rs.getString("publisheremail");
                                //Display values
                                
                                System.out.printf(displayFormat1,
                                dispNull(publishername), dispNull(publisheraddress), dispNull(publisherphone), dispNull(publisheremail));
                            }
                        break;//done
                    }//List all Publishers// regulare code //List all Publishers// regulare code 
                    //to do= input validation and action messages.

                    case 4: 
                    {
                        System.out.print("Enter Publisher name: ");
                        String pName = in.nextLine().toUpperCase();

                        String checkPubSQL = "Select publisherName FROM publishers";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(checkPubSQL);
                        boolean match = false;

                        // Check to see if publisherName matches any existing publisherNames
                        while (!match) {
                            try {
                                // iterate through existing names and check for match
                                // if no match is found exception is thrown and process repeats
                                // if match found while is broken and proceed forward
                                while (rs.next()) {
                                    String pub = rs.getString("publisherName").toUpperCase();
                                    if(pub.equals(pName))
                                        match = true;
                                    if (match) {
                                        break;
                                    }
                                }
                                if(!match)
                                    throw new Exception();
                            } catch (Exception e) {
                                System.out.println("Error: Publisher not found. Enter valid publisher:");
                                System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                                int exitchoice=CheckInput.getIntRange(1,2);
                                if(exitchoice==2)break;
                                System.out.println("Enter valid publisher:");
                                pName = in.nextLine().toUpperCase();

                                checkPubSQL = "Select publisherName FROM publishers";
                                stmt = conn.createStatement();
                                rs = stmt.executeQuery(checkPubSQL);
                            }
                        }

                        //String sql;
                        String sql = "SELECT publisherName, publisherAddress, publisherPhone, publisherEmail, groupName, headwriter, yearformed, Subject, bookTitle, yearPublished, numberPages FROM Publishers INNER JOIN (Books NATURAL JOIN WritingGroups) USING(PUBLISHERNAME) " +
                                     "WHERE UPPER(PUBLISHERNAME) = ?";

                        preparedStatement = conn.prepareStatement(sql);
                        preparedStatement.setString(1, pName);
                        rs = preparedStatement.executeQuery();

                        //String case4Format = "%-25s%-70s%-20s%-40s\n";
                        //System.out.printf(case4Format, "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");
                        String case4Format = "%-25s%-65s%-20s%-40s%-25s%-25s%-20s%-10s%-25s%-20s%-20s\n";
                        System.out.printf(case4Format, "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email", 
                                          "Writing Group", "Head Writer", "Year Formed", 
                                          "Subject", "Book Title", "Year Published", "Number of Pages");


                        while (rs.next()) {

                            String publisherName = rs.getString("publisherName");
                            String publisherAddress = rs.getString("publisherAddress");
                            String publisherPhone = rs.getString("publisherPhone");
                            String publisherEmail = rs.getString("publisherEmail");

                            String groupname = rs.getString("groupname");
                            String headwriter = rs.getString("headwriter");
                            String yearformed = String.valueOf(rs.getInt("yearformed"));
                            String subject = rs.getString("subject");

                            String bookTitle = rs.getString("bookTitle");
                            int yearPublished = rs.getInt("yearPublished");
                            int numberPages = rs.getInt("numberPages");



                            System.out.printf(case4Format,
                                    dispNull(publisherName), dispNull(publisherAddress), dispNull(publisherPhone), dispNull(publisherEmail),
                                    dispNull(groupname), dispNull(headwriter), dispNull(yearformed), dispNull(subject),
                                    dispNull(bookTitle), dispNull(Integer.toString(yearPublished)), dispNull(Integer.toString(numberPages))); 

                        }
                        break;

                    }//List all data for a publisher specified by user// prepared statemen
                    //to do= input validation and action messages.

                     case 5: {
                        System.out.println("Creating statement...");
                        stmt = conn.createStatement();
                        String sql;
                        sql = "SELECT booktitle FROM books";
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                                //Retrieve by column name
                                String booktitle = rs.getString("booktitle");
                                //Display values
                                String displayFormat1="%-25s\n";
                                System.out.printf(displayFormat1,
                                dispNull(booktitle));
                            }
                        break;
                    }//List all book titles// regular code
                    //to do= input validation and action messages.

               case 6: {
                      
                      boolean match = false;
                      
                      // Check to see if bookTitle matches any existing bookTitles
                      String input="";
                      int exitchoice=0;
                      while (!match) {
                          System.out.println("Select data by Book? Type Book Name");
                        input = in.nextLine().toUpperCase();
                      
                      String checkBookSQL = "Select bookTitle FROM Books";
                      stmt = conn.createStatement();
                      rs = stmt.executeQuery(checkBookSQL);
                          
                          try {
                              // iterate through existing titles and check for match
                              // if no match is found exception is thrown and process repeats
                              // if match found while is broken and proceed forward
                              while (rs.next()) {
                                  String book = rs.getString("bookTitle").toUpperCase();
                                  if(book.equals(input))
                                      match = true;
                                  if (match) {
                                      break;
                                  }
                              }
                              if(!match)
                                  throw new Exception();
                          } catch (Exception e) {
                              System.out.println("Error: Book title not found.");
                              System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                              exitchoice=CheckInput.getIntRange(1,2);
                              if(exitchoice==2)break;
                            
                          }
                      }
                      if(exitchoice==2){break;}

                      // prompts user for the second part of the full key
                      //System.out.println("Enter (Writing Group) ");
                      String secondInput; 
                      int secondKey =1;
                      boolean secondMatch = false;
                      if(secondKey == 1) {
                         
                          
                          // iterates through result set to find a matching group name
                          while(!secondMatch) {
                             System.out.println("Enter: Writing group");
                          secondInput = in.nextLine().toUpperCase();
                          
                          // get result set from designated query
                          String checkSecondKey = "SELECT groupName FROM Books NATURAL JOIN WritingGroups where UPPER(bookTitle) = '"
                                  + input + "' AND UPPER(GroupName) = ?";
                          preparedStatement = conn.prepareStatement(checkSecondKey);
                          preparedStatement.setString(1,secondInput);
                          rs = preparedStatement.executeQuery();
                              while (rs.next()) {
                                  String group = rs.getString("groupName").toUpperCase();
                                  
                                  if(group.equals(secondInput))
                                      secondMatch = true;
                                  if (secondMatch) {
                                      break;
                                  }
                              }
                              // returns back to menu if no match is found
                              if(!secondMatch) {
                                  System.out.println("Failed to enter valid group name. Returning to menu.");
                                  System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                                    exitchoice=CheckInput.getIntRange(1,2);
                                    if(exitchoice==2)break;
                              }
                          }
                          
                      } 
                      else {
                         
                          
                          // iterates through result set to find a matching publisher
                          while(!secondMatch) {
                               System.out.println("Enter: Publisher");
                          secondInput = in.nextLine().toUpperCase();
                          
                          // get result set from designated query
                          String checkSecondKey = "SELECT publisherName FROM Books NATURAL JOIN Publishers where UPPER(bookTitle) = '"
                                  + input + "' AND UPPER(publisherName) = ?";
                          preparedStatement = conn.prepareStatement(checkSecondKey);
                          preparedStatement.setString(1,secondInput);
                          rs = preparedStatement.executeQuery();
                              while (rs.next()) {
                                  String pub = rs.getString("publisherName").toUpperCase();
                                  
                                  if(pub.equals(secondInput))
                                      secondMatch = true;
                                  if (secondMatch) {
                                      break;
                                  }
                              }
                              
                              // returns back to menu if no match is found
                              if(!secondMatch) {
                                 
                                  System.out.println("Failed to enter valid publisher. ");
                                  System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                                    exitchoice=CheckInput.getIntRange(1,2);
                                    if(exitchoice==2)break;
                              }
                          }
              
                      }

                      
                      String searchbybooktitle = "SELECT bookTitle, yearPublished, numberPages, groupName, headwriter, yearformed, Subject, publisherName, publisherAddress, publisherPhone, publisherEmail FROM Books NATURAL JOIN WritingGroups NATURAL JOIN Publishers WHERE UPPER(bookTitle) = ?";
                      preparedStatement = conn.prepareStatement(searchbybooktitle);
                      preparedStatement.setString(1,input);
                      rs=preparedStatement.executeQuery();
                     
                      String case6Format = "%-25s%-20s%-20s%-25s%-25s%-15s%-10s%-25s%-65s%-20s%-40s\n";
                      System.out.printf(case6Format, "Book Title", "Year Published", "Number of Pages",
                              "Writing Group", "Head Writer", "Year Formed", "Subject",
                              "Publisher Name", "Publisher Address", "Publisher Phone", "Publisher Email");
                      
                      while(rs.next()){
                            String bookTitle = rs.getString("bookTitle");
                            int yearPublished = rs.getInt("yearPublished");
                            int numberPages = rs.getInt("numberPages");
                            
                            String groupname = rs.getString("groupname");
                            String headwriter = rs.getString("headwriter");
                            String yearformed = String.valueOf(rs.getInt("yearformed"));
                            String subject = rs.getString("subject");
                          
                            String publisherName = rs.getString("publisherName");
                            String publisherAddress = rs.getString("publisherAddress");
                            String publisherPhone = rs.getString("publisherPhone");
                            String publisherEmail = rs.getString("publisherEmail");

                            

                            
            
                              System.out.printf(case6Format,
                                dispNull(bookTitle), dispNull(Integer.toString(yearPublished)), dispNull(Integer.toString(numberPages)), 
                                dispNull(groupname), dispNull(headwriter), dispNull(yearformed), dispNull(subject),
                                dispNull(publisherName), dispNull(publisherAddress), dispNull(publisherPhone), dispNull(publisherEmail)); 
                      }
                      break;
                    }
                    //List all the data for a single book specified by the user//prepared statment
                    //to do= input validation and action messages.
                    //List all the data for a single book specified by the user//prepared statment
                    //to do= input validation and action messages.
              case 7: // only works if the user inputs an exisiting writing group and publisher
                    {
                      System.out.println("Select Writing group name");
                      String groupName = in.nextLine().toUpperCase();
                      
                      String checkGroupSQL = "Select groupname FROM writinggroups";
                      stmt = conn.createStatement();
                      rs = stmt.executeQuery(checkGroupSQL);
                      boolean match = false;
                      // Check to see if bookTitle matches any existing bookTitles
                      while (!match) {
                          try {
                              // iterate through existing titles and check for match
                              // if no match is found exception is thrown and process repeats
                              // if match found while is broken and proceed forward
                              while (rs.next()) {
                                  String group = rs.getString("groupname").toUpperCase();
                                  if(group.equals(groupName))
                                      match = true;
                                      
                                  if (match) {
                                      groupName = rs.getString("groupname");
                                      break;
                                  }
                              }
                              if(!match)
                                  throw new Exception();
                          } catch (Exception e) {
//                              System.out.println("Error: Writting Group does not exist Enter valid writing group:");
//                              groupName = in.nextLine().toUpperCase();
//                              rs = stmt.executeQuery(checkGroupSQL);
                              
                                 System.out.println("Error: Group not found.");
                                System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                                int exitchoice=CheckInput.getIntRange(1,2);
                                if(exitchoice==2)break;
                                System.out.println("Enter valid group:");
                                groupName = in.nextLine().toUpperCase();

                                checkGroupSQL = "Select groupName FROM WritingGroups";
                                stmt = conn.createStatement();
                                rs = stmt.executeQuery(checkGroupSQL);
                              
                          }
                          
                      }
                        
                        System.out.print("Enter Publisher name: ");
                    String publisherName = in.nextLine().toUpperCase();
                     
                    String checkPubSQL = "Select publisherName FROM publishers";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(checkPubSQL);
                    match = false;
                    
                    // Check to see if publisherName matches any existing publisherNames
                    while (!match) {
                        try {
                            // iterate through existing names and check for match
                            // if no match is found exception is thrown and process repeats
                            // if match found while is broken and proceed forward
                            while (rs.next()) {
                                String pub = rs.getString("publisherName").toUpperCase();
                                if(pub.equals(publisherName))
                                    match = true;
                                    
                                if (match) {
                                    publisherName = rs.getString("publisherName");
                                    break;
                                }
                            }
                            if(!match)
                                throw new Exception();
                        } catch (Exception e) {
//                            System.out.println("Error: Publisher not found. Enter valid publisher:");
//                            publisherName = in.nextLine().toUpperCase();
//
//                            checkPubSQL = "Select publisherName FROM publishers";
//                            stmt = conn.createStatement();
//                            rs = stmt.executeQuery(checkPubSQL);
                            
                               System.out.println("Error: Publisher not found. Enter valid publisher:");
                                System.out.println("Would you like to (1)re-enter a selection or go (2)back-to-menu");
                                int exitchoice=CheckInput.getIntRange(1,2);
                                if(exitchoice==2)break;
                                System.out.println("Enter valid publisher:");
                                publisherName = in.nextLine().toUpperCase();

                                checkPubSQL = "Select publisherName FROM publishers";
                                stmt = conn.createStatement();
                                rs = stmt.executeQuery(checkPubSQL);
                        }
                    }
                        
                        System.out.print("Enter book title: ");
                        String newTitle = CheckInput.getString();
                        
                        String checkBookSQL = "Select bookTitle FROM Books";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(checkBookSQL);
                        boolean found = false;
                        
                        // Check to see if bookTitle matches any existing bookTitles
                        while (!found)
                        {
                            try 
                            {
                              // iterate through existing names and check for match
                                // if no match is found exception is thrown and process repeats
                                // if match found while is broken and proceed forward
                                while (rs.next()) 
                                {
                                    String existingBook = rs.getString("bookTitle");
                                    if(existingBook.toUpperCase().equals(newTitle.toUpperCase()))
                                    {
                                        throw new Exception();
                                    }
                                }
                                
                                if(!found)
                                {
                                    break;
                                }
                          } catch (Exception e) {
                              //System.out.println("Error: Book does not exist Enter valid book title:");
                              System.out.print("Book already exists, please enter a different title: ");
                              newTitle = CheckInput.getString();
                              rs = stmt.executeQuery(checkBookSQL);
                          } 
                        }
                        
                        System.out.print("Enter the year it was published: ");
                        int yearPublished = CheckInput.getIntRange(1440,2021);//year guttenberg invented printing press
                        System.out.print("Enter number of pages: ");
                        int numberPages = CheckInput.getIntRange(0,Integer.MAX_VALUE);
                        String sql;
                        sql = "INSERT INTO Books (groupName, publisherName, bookTitle, yearPublished, numberPages) VALUES (?,?,?,?,?)";
                        
                        try
                        {
                            preparedStatement = conn.prepareStatement(sql);
                            preparedStatement.setString(1,groupName);
                            preparedStatement.setString(2,publisherName);
                            preparedStatement.setString(3,newTitle);
                            preparedStatement.setInt(4,yearPublished);
                            preparedStatement.setInt(5,numberPages);
                            preparedStatement.execute();
                            System.out.println("Book has been entered");
                        }
                        catch (SQLException e)
                        {
                            System.out.println("Invalid entry, Book is in Database already\n going back to menu.");
                        }
                      
                        break;
                    }

                    //Insert a new publisher// prepared statemd
                    //to do= input validation and action messages.
                    //double check outputs
                    case 8: {
                    System.out.println("Enter (1) to add publisher or (2) to modify and update");
                    int userChoice = CheckInput.getIntRange(1, 2);
                    if (userChoice == 1) {
                        System.out.println("Which Publisher would you like to add");
                        String name = in.nextLine();
                        // checks if user publisher name is already in data set
                        String checkPublisherSQL = "Select publisherName FROM Publishers";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(checkPublisherSQL);
                        boolean foundDupe = false;

                        while (!foundDupe) {
                            // iterates through result set and checks for a duplicate
                            while (rs.next()) {
                                String pub = rs.getString("publisherName");
                                if (pub.toUpperCase().equals(name.toUpperCase())) {
                                    System.out.println("Error: Attmempting to add dupplicate publisher. Re-enter nondupicate publisher");
                                    rs = stmt.executeQuery(checkPublisherSQL);
                                    name = in.nextLine();
                                    foundDupe = true;
                                }
                            }
                            if (!foundDupe) {
                                break;
                            }
                        }

                        System.out.println("Enter address");
                        String Address = in.nextLine();
                        System.out.println("Enter phone number");
                        String phone = in.nextLine();// check for atleast 10 numbers 
                        System.out.println("Enter email");
                        String email = in.nextLine();//check for @ and .com at the end 

                        String insertPublisher = "insert into publishers(publishername,publisheraddress,publisherphone,publisherEmail) values(?,?,?,?)";

                        preparedStatement = conn.prepareStatement(insertPublisher);
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, Address);
                        preparedStatement.setString(3, phone);
                        preparedStatement.setString(4, email);
                        int row = preparedStatement.executeUpdate();
                        System.out.println("Publisher has been added");
                    } else {
                        System.out.println("Enter publisher you want to updated into the books.");
                        String name = in.nextLine();

                        // checks if user publisher name is already in data set
                        String checkPublisherSQL = "Select publisherName FROM Publishers";
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery(checkPublisherSQL);
                        boolean foundDupe = false;

                        // get publisher to be updated into books + validate it
                        while (!foundDupe) {
                            // iterates through result set and checks for a duplicate
                            while (rs.next()) {
                                String pub = rs.getString("publisherName");
                                if (pub.toUpperCase().equals(name.toUpperCase())) {
                                    foundDupe = true;
                                    break;
                                }
                            }
                            if (!foundDupe) {
                                System.out.println("Could not locate publisher in data set. Enter valid publisher.");
                                rs = stmt.executeQuery(checkPublisherSQL);
                                name = in.nextLine();
                            }
                        }

                        System.out.println("Enter publisher you want to be removed from books");
                        String secondName = in.nextLine();
                        foundDupe = false;
                        rs = stmt.executeQuery(checkPublisherSQL);
                        // get publisher to be updated out of books + validate it
                        while (!foundDupe) {
                            // iterates through result set and checks for a duplicate
                            while (rs.next()) {
                                String pub = rs.getString("publisherName");
                                if (pub.toUpperCase().equals(secondName.toUpperCase())) {
                                    foundDupe = true;
                                    break;
                                }
                            }
                            if (!foundDupe) {
                                System.out.println("Could not locate publisher in data set. Enter valid publisher.");
                                rs = stmt.executeQuery(checkPublisherSQL);
                                secondName = in.nextLine();
                            }
                        }

                        String alterPub = "UPDATE Books SET publisherName = ? WHERE publisherName = ?";
                        preparedStatement = conn.prepareStatement(alterPub);
                        preparedStatement.setString(1, name);
                        preparedStatement.setString(2, secondName);
                        preparedStatement.execute();
                        System.out.println("Successfully updated books.");

                    }

                    break;
                    }

                    
                    case 9:
                    {
                      System.out.print("Enter the book you wish to remove: ");
                    String bookTitle = CheckInput.getString();

                    String checkBookSQL = "SELECT bookTitle FROM Books";
                    stmt = conn.createStatement();
                    rs = stmt.executeQuery(checkBookSQL);
                    boolean match = false;

                    // Check to see if bookTitle matches any existing bookTitles
                    while (!match) {
                        try {
                            // iterate through existing titles and check for match
                            // if no match is found exception is thrown and process repeats
                            // if match found while is broken and proceed forward
                            while (rs.next()) {
                                String bookInTable = rs.getString("bookTitle");
                                if (bookInTable.toUpperCase().equals(bookTitle.toUpperCase())) {
                                    match = true;
                                }

                                if (match) {
                                    bookTitle = rs.getString("bookTitle");
                                    break;
                                }
                            }
                            if (!match) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Error: Book does not exist Enter valid book title:");
                            bookTitle = CheckInput.getString();
                            rs = stmt.executeQuery(checkBookSQL);
                        }

                    }

                    System.out.println("Enter writing group associated with book");
                    String group = in.nextLine();
                    String checkGroupSQL = "SELECT groupName FROM Books WHERE bookTitle = ?";
                    preparedStatement = conn.prepareStatement(checkGroupSQL);
                    preparedStatement.setString(1, bookTitle);
                    rs = preparedStatement.executeQuery();
                    match = false;

                    // Check to see if bookTitle matches any existing bookTitles
                    while (!match) {
                        try {
                            // iterate through existing writing group names and check for match
                            while (rs.next()) {

                                String groupName = rs.getString("groupName");
                                if (group.toUpperCase().equals(groupName.toUpperCase())) {
                                    group = rs.getString("groupName");
                                    match = true;
                                    break;
                                }

                            }
                            if (!match) {
                                throw new Exception();
                            }
                        } catch (Exception e) {
                            System.out.println("Error: Book does not exist with entered group name. Re-enter valid group name:");
                            group = CheckInput.getString();
                            rs = preparedStatement.executeQuery();
                        }

                    }

                    String removeBook = "DELETE FROM Books WHERE bookTitle = ? AND groupName = ?";
                    preparedStatement = conn.prepareStatement(removeBook);
                    preparedStatement.setString(1, bookTitle);
                    preparedStatement.setString(2, group);

                    int row = preparedStatement.executeUpdate();
                    if (row > 0) {
                        System.out.println("Selection has been deleted");
                    } else {
                        System.out.println("No deletions were made. Returning to menu.\n" + bookTitle + group);
                    }
                    break;
                    }//to do= input validation and action messages. [message one: item is deleted,, message 2: item is not in list, message 3: would you like to chekc list??. ]



                    case 10: {System.out.println("Goodbye");
                        System.exit(0);}//exit
                }
                
        
            }//end of while
            try {
         
            //STEP 6: Clean-up environment
            if(rs!=null)rs.close();
            if(stmt!=null)stmt.close();
            if(preparedStatement!=null)preparedStatement.close();
            conn.close();
        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException se2) {
            }// nothing we can do
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
        }//end try

            //STEP 4: Execute a query
            

            //STEP 5: Extract data from result set
            
    }//end main

}//end FirstExample}

//}//end FirstExample}
