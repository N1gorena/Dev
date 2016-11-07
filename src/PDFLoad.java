import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.json.simple.JSONObject;


import com.mysql.jdbc.PreparedStatement;

@MultipartConfig
public class PDFLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String serverURL = "jdbc:mysql://localhost:3306/capstonedb?useSSL=false";
	private final String sqlInsertString = "Insert into documents (FileName,FileURL,Category) values (?,?,?)";
	private final String sqlSelectString = "Select Id from documents where FileName = ? and FileURL = ? and Category = ?";
	private final String pdfBaseFolder = "/var/www/html/pdfs/";

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json");
		JSONObject returnable = new JSONObject();
		returnable.put("MESSAGE", "No GET support on this endpoint");
		PrintWriter output = response.getWriter();
		output.print(returnable);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//doGet(request, response);
		response.setContentType("application/json");
		String responseMessage = "NONE";
		JSONObject returnable = new JSONObject();
		Connection dbConn;
		
		Part pdfPart = request.getPart("PDF");
		String pdfFileName = (request.getParameter("FILENAME") == null)?request.getParameter("FILENAME"):pdfPart.getSubmittedFileName();
		String pdfCategory = request.getParameter("CATEGORY");
		
		StringBuilder pdfSaveLocation = new StringBuilder();
		pdfSaveLocation.append(pdfBaseFolder);
		pdfSaveLocation.append(pdfCategory+"/");
		pdfSaveLocation.append(pdfFileName);
		try{
		pdfPart.write(pdfSaveLocation.toString());
		Class.forName("com.mysql.jdbc.Driver");
		dbConn = DriverManager.getConnection(serverURL, "root","Trojans17");
		PreparedStatement query = (PreparedStatement) dbConn.prepareStatement(sqlInsertString);
		query.setString(1, pdfFileName);
		query.setString(2, pdfSaveLocation.toString());
		query.setString(3, pdfCategory);
		query.executeUpdate();
		PreparedStatement insertedElementID = (PreparedStatement) dbConn.prepareStatement(sqlSelectString);
		insertedElementID.setString(1, pdfFileName);
		insertedElementID.setString(2, pdfSaveLocation.toString());
		insertedElementID.setString(3, pdfCategory);
		ResultSet elementRow = insertedElementID.executeQuery();
		if(elementRow.first()){
			int docID = elementRow.getInt("Id");
		}
		
		
		
		}
		catch(IOException e){
			
		}catch (ClassNotFoundException e) {
			responseMessage = e.getMessage();
			e.printStackTrace();
		} catch (SQLException e) {
			responseMessage = e.getMessage();
			e.printStackTrace();
		}
		returnable.put("MESSAGE", responseMessage);
		response.getWriter().print(returnable);
	}
}
