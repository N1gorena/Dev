

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.mysql.jdbc.PreparedStatement;


public class ListPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String serverURL = "jdbc:mysql://localhost:3306/capstonedb?useSSL=false";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection dbConn;
		PreparedStatement genStatement = null;
		
		response.setContentType("application/json");
		PrintWriter output = response.getWriter();
		String message = "None";
		JSONObject obj = new JSONObject();
		ResultSet results = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			dbConn = DriverManager.getConnection(serverURL,"root","Trojans17");
			
			switch(request.getParameter("Type")){
			case "1": 
				genStatement = (PreparedStatement) dbConn.prepareStatement("SELECT DISTINCT Category FROM documents;");
				results = genStatement.executeQuery();
				ArrayList<String> resultObject = new ArrayList<>();
				while(results.next()){
					resultObject.add(results.getString(1));
				}
				obj.put("Message", message);
				obj.put("Catagories", resultObject);
				obj.put("Success", true);
				
				break;
			case "2": 
				genStatement = (PreparedStatement) dbConn.prepareStatement("SELECT FileName,FileURL, Id FROM documents where Category = ?");
				genStatement.setString(1, request.getParameter("Category"));
				results = genStatement.executeQuery();
				ArrayList<String[]> documents = new ArrayList<>();
				while(results.next()){
					String[] boofer = {results.getString(1),results.getString(2),results.getString(3)};
					documents.add(boofer);
				}
				obj.put("Message", message);
				obj.put("PDFS", documents);
				obj.put("Success", true);
				break;
			case "3": 
				//TODO Not sure this one is needed due to the fact that the METATAG thing is up in the air.
				
				break;
			case "4":
				genStatement = (PreparedStatement) dbConn.prepareStatement("SELECT UniqueID,Filepath,PDFTitle FROM filled_pdfs where UniqueIDOfUser = ?");
				genStatement.setInt(1, Integer.parseInt(request.getParameter("userUniqueID")));
				results = genStatement.executeQuery();
				ArrayList<String[]> filledPDFS = new ArrayList<>();
				while(results.next()){
					String[] boofer = {results.getString(1),results.getString(2),results.getString(3)};
					filledPDFS.add(boofer);
				}
				obj.put("Message", message);
				obj.put("UserDocs", filledPDFS);
				obj.put("Success", true);
				break;
			default:
				message = "IMPROPER TYPE NUMBER ON REQUEST!";
				obj.put("Message", message);
				obj.put("Success", false);
				
			}
			
			output.print(obj);
			
		}
		catch(ClassNotFoundException e) {
			obj.put("Message", e.getMessage());
			obj.put("Success", false);
			e.printStackTrace();
		} catch (SQLException e) {
			obj.put("Message", e.getMessage());
			obj.put("Success", false);
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
