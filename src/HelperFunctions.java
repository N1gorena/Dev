import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

public class HelperFunctions {

    private static final String endPDFLoc = "/var/www/html/pdfs/";
    public static int getStorageLocation(){
    	return endPDFLoc.length();
    }

	
	public static String listFields(PDDocument doc,Map<String,String[]> fieldMap) throws Exception {
		PDDocumentCatalog catalog = doc.getDocumentCatalog();
		PDAcroForm form = catalog.getAcroForm();
		java.util.List<PDField> fields = form.getFields();
		
		for(PDField field: fields) {
			String name = field.getFullyQualifiedName();
			if(fieldMap.containsKey(name) && fieldMap.get(name)[0] != null ){
				field.setValue(fieldMap.get(name)[0]);
			}
			else{
				field.setValue("N/A");
			}
		}
		StringBuilder tempPath = new StringBuilder();
		tempPath.append(endPDFLoc);
		String pdfFileNameSansExt = fieldMap.get(FillPDF.pdfNameKey)[0];
		int dotIndex = pdfFileNameSansExt.lastIndexOf(".");
		tempPath.append(pdfFileNameSansExt.substring(0, dotIndex)+"_");
		tempPath.append(fieldMap.get(FillPDF.pdfIDKey)[0]+"_");
		tempPath.append(new SimpleDateFormat("yyyyMMddhhmm").format(new Date()));
		tempPath.append(".pdf");
		form.flatten();
		doc.save(tempPath.toString());
		doc.close();
		Set<PosixFilePermission> otherReadablePermissionSet = new HashSet<>();
		otherReadablePermissionSet.add(PosixFilePermission.OWNER_READ);
		otherReadablePermissionSet.add(PosixFilePermission.OWNER_WRITE);
		otherReadablePermissionSet.add(PosixFilePermission.GROUP_READ);
		otherReadablePermissionSet.add(PosixFilePermission.OTHERS_READ);
		Files.setPosixFilePermissions(Paths.get(tempPath.toString()), otherReadablePermissionSet );
		return tempPath.toString();
	}
	/*
	 * <--Storage File Naming Convention-->
	 * [Base pdf name]_[PDFID]_[TIMESTAMP].pdf
	 * 
	 * <--Storage Location-->
	 * /var/www/html/ so Apache can handle access to the PDF's online.
	 */
}
