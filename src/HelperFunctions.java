import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class HelperFunctions {

	public static void listFields(PDDocument doc,Map<String,String[]> fieldMap) throws Exception {
		PDDocumentCatalog catalog = doc.getDocumentCatalog();
		PDAcroForm form = catalog.getAcroForm();
		java.util.List<PDField> fields = form.getFields();
		for(PDField field: fields) {
			String name = field.getFullyQualifiedName();
			if(fieldMap.containsKey(name) && fieldMap.get(name)[0] != null ){
				field.setValue(fieldMap.get(name)[0]);
			}
			else{
				field.setValue("OOGABOOGA");
			}
		}
		String tempPath;
		tempPath = "/home/PDF_Processing/TestingOnServer.pdf";
		doc.save(tempPath);
		doc.close();
	}
}
