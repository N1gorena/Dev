

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
			
			File pdfDoc = new File("/home/PDF_Processing/test.pdf");
			if(pdfDoc.exists()){
					obj.put("Message","Exists");
			}
			else{
				obj.put("Message","DNE DEV");
			}
			
			PDDocument truePDF;
			try {
				Class.forName("org.apache.pdfbox.pdmodel.PDDocument");
				truePDF = PDDocument.load(pdfDoc);
				HelperFunctions.listFields(truePDF,paramMap);
				obj.put("Mensaje","Cumpleto");


			} catch (ClassNotFoundException e1) {
				obj.put("Mensaje","CNF"+e1.getMessage());
				e1.printStackTrace();
			}catch (Exception e) {
				obj.put("Mensaje","EEEEE"+e.getMessage());
				e.printStackTrace();
			}
			
		
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
