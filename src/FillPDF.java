

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONObject;


public class FillPDF extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static String serverURL = "jdbc:mysql://localhost:3306/capstonedb?useSSL=false";
    public static final String pdfNameKey = "PDFKEY";
    private final String userIDKey = "USERID";
    public static final String pdfIDKey = "PDFID";
    private final String basePDFLoc = "/home/PDF_Processing/";
   
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter outputWriter  = response.getWriter();
		Connection dbConn;
		String errMessage = "None";
		JSONObject obj = new JSONObject();

			//dbConn = DriverManager.getConnection(serverURL, "root", "Trojans17");
			Map<String,String[]> paramMap = request.getParameterMap();
			StringBuilder fileLocation = new StringBuilder();

			if(paramMap.containsKey(pdfNameKey)){
				fileLocation.append(basePDFLoc);
				fileLocation.append(paramMap.get(pdfNameKey)[0]);
			}
			else{
				fileLocation.append("/noexist/");
			}
			
			if(!paramMap.containsKey(userIDKey)){
				errMessage = "No User info given.";

			}
			
			File pdfDoc = new File(fileLocation.toString());
			if(!pdfDoc.exists()){
				errMessage = "FILE DOES NOT EXIST";
			}
			
			PDDocument truePDF;
			try {
				Class.forName("org.apache.pdfbox.pdmodel.PDDocument");
				truePDF = PDDocument.load(pdfDoc);
				HelperFunctions.listFields(truePDF,paramMap);

			} catch (ClassNotFoundException e1) {
				errMessage = "CNF"+e1.getMessage();
				e1.printStackTrace();
			}catch (Exception e) {
				errMessage = "EEEEE"+e.getMessage();
				e.printStackTrace();
			}
			
		obj.put("Message",errMessage);
		outputWriter.print(obj);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
