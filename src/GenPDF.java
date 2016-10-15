

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.mysql.jdbc.PreparedStatement;


public class GenPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String serverURL = "jdbc:mysql://localhost:3306/capstonedb?useSSL=false";
	//TODO finish Statement
	private final String query = "SELECT field_name,type,field_option FROM pdf_structure where PdfID = ?";
	private Map<String,FieldElement> fieldData;
   
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
		fieldData = new HashMap<>();
		if(request.getParameter("PdfID") != null){
			try {
				//Recieving key PdfID
				Class.forName("com.mysql.jdbc.Driver");
				dbConn = DriverManager.getConnection(serverURL,"root","Trojans17");
				Integer id = Integer.valueOf(request.getParameter("PdfID") );
				
				genStatement =  (PreparedStatement) dbConn.prepareStatement(query);
				genStatement.setInt(1, id);
				results = genStatement.executeQuery();
				while(results.next()){
					FieldElement tempFieldElement;
					String nombre = results.getString("field_name");
					String tipo = results.getString("type");
					String val = results.getString("field_option");
					if(fieldData.containsKey(nombre)){
						tempFieldElement = fieldData.get(nombre);
					}
					else{
						tempFieldElement = new FieldElement(nombre, tipo);
					}
					tempFieldElement.addValue(val);
					fieldData.put(nombre, tempFieldElement);
				}
				
				Iterator<String> sI = fieldData.keySet().iterator();
				while(sI.hasNext()){
					FieldElement f = fieldData.get(sI.next());
					JSONObject fieldEle = new JSONObject();
					JSONArray fieldOptions = new JSONArray();
					fieldEle.put("type", f.mType);
					fieldOptions.addAll(f.mValues);
					fieldEle.put("value", fieldOptions);
					obj.put(f.mName, fieldEle);
					
				}
				obj.put("Message", "Success");
				obj.put("Success", true);
					
			} catch (SQLException e) {
				obj.put("Message", e.getMessage());
				obj.put("Success", false);
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				obj.put("Message", e.getMessage());
				obj.put("Success", false);
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private class FieldElement{
		String mName;
		String mType;
		ArrayList<String> mValues;
		public FieldElement(String fieldName,String fieldType){
			mName = fieldName;
			mType = fieldType;
			mValues = new ArrayList<>();
		}
		public void addValue(String val){
			mValues.add(val);
		}
	}

}
