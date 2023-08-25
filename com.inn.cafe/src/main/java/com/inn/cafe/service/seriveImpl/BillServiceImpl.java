package com.inn.cafe.service.seriveImpl;

import com.inn.cafe.JWT.JwtFilter;
import com.inn.cafe.POJO.Bill;
import com.inn.cafe.constants.CafeConstants;
import com.inn.cafe.dao.BillDao;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillDao billDao;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generatedReport");
        try {
            String fileName;
            if (validateRequestMap(requestMap)) {
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    fileName = (String) requestMap.get("uuid");
                } else {
                    fileName = CafeUtils.getUUID();//We r going to write a new method in the CafeUtils.
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }

                String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number: " + requestMap.get("contactNumber") +
                        "\n" + "Email: " + requestMap.get("email") + "\n" + "Payment Method: " + requestMap.get("paymentMethod");

                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION + "\\" + fileName + ".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph("Cafe Management System", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);

                //As our data is in JSON form we create JSONArray
                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));//We will create a new method in CafeUtils.
                for (int i = 0; i < jsonArray.length(); i++) {
                    //We r creating another method which will convert the JSONArray to Json by getMapFromJson()
                    addRow(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));//This line will extract one after another values from JsonArray.
                }
                document.add(table);

                Paragraph footer = new Paragraph("Total :" + requestMap.get("totalAmount") + "\n"
                        + "Thank you for Visiting.. Please Visit again!!", getFont("Data"));
                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);

            }
            return CafeUtils.getResponseEntity("Required Data not found!..", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");
    }

    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());//We are setting the current user.
            billDao.save(bill);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setRectangleInPdf(Document document) throws DocumentException {//Throws DocumentException if document is not found
        log.info("Inside setRectangleInPdf");
        Rectangle rect = new Rectangle(577, 825, 18, 15);
        rect.enableBorderSide(1);//For all the 4 dimensions mentioned in the above object.
        rect.enableBorderSide(2);
        rect.enableBorderSide(4);
        rect.enableBorderSide(8);
        rect.setBorderColor(BaseColor.BLACK);
        rect.setBorderWidth(1);
        document.add(rect);
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Sub Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);//It is actually running stream loop and adding all features to each table header columns one after another.
                });
    }

    private void addRow(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list = new ArrayList<>();
        if (jwtFilter.isAdmin()) {
            list = billDao.getAllBills();//For admins all the bills are shown.
        } else {
            list = billDao.getBillByUserName(jwtFilter.getCurrentUser());//For other users particular bills only can be seen/visible.
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Insider getPdf : requestMap {}", requestMap);//All the data present in requestMap will be printed with the help of this line, so that we can track it while debugging if required.
        try {
            byte[] byteArray = new byte[0];
            if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            String filePath = CafeConstants.STORE_LOCATION + "\\" + (String) requestMap.get("uuid") + ".pdf";
            if (CafeUtils.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            } else {
                requestMap.put("isGenerate", false);
                generateReport(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional optional = billDao.findById(id);
            if (!optional.isEmpty()){
                billDao.deleteById(id);
                return CafeUtils.getResponseEntity("Bill Deleted Successfully!..", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Bill id doesn't exist!..", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
