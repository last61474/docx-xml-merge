package com.dbs.bazr;

import org.docx4j.Docx4J;
import org.docx4j.model.datastorage.BindingHandler;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.utils.XPathFactoryUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class DocGenerator {
    public static void main(String[] args) throws Docx4JException, FileNotFoundException {
        // Without Saxon, you are restricted to XPath 1.0
        boolean USE_SAXON = true; // set this to true; add Saxon to your classpath, and uncomment below

        String input_DOCX = "C:\\MDrive\\work\\dbs\\fix\\office\\opendope\\file-dop2.docx";
//		String input_DOCX = System.getProperty("user.dir") + "/sample-docs/databinding/invoice_Saxon_XPath2.docx";

        String input_XML = "C:\\MDrive\\work\\dbs\\fix\\office\\opendope\\fields-dope.xml";


        // resulting docx
        String OUTPUT_DOCX = "C:\\MDrive\\work\\dbs\\fix\\office\\opendope\\fields-dope-output.docx";
        String OUTPUT_PDF = "C:\\MDrive\\work\\dbs\\fix\\office\\opendope\\fields-dope-output.pdf";

		if (USE_SAXON) XPathFactoryUtil.setxPathFactory(
		        new net.sf.saxon.xpath.XPathFactoryImpl());


        // Load input_template.docx
        WordprocessingMLPackage wordMLPackage = Docx4J.load(new File(input_DOCX));

        // Open the xml stream
        FileInputStream xmlStream = new FileInputStream(new File(input_XML));

        // Do the binding:
        // FLAG_NONE means that all the steps of the binding will be done,
        // otherwise you could pass a combination of the following flags:
        // FLAG_BIND_INSERT_XML: inject the passed XML into the document
        // FLAG_BIND_BIND_XML: bind the document and the xml (including any OpenDope handling)
        // FLAG_BIND_REMOVE_SDT: remove the content controls from the document (only the content remains)
        // FLAG_BIND_REMOVE_XML: remove the custom xml parts from the document

        // BindingHyperlinkResolver is used by default
        // BindingHandler.setHyperlinkResolver(new BindingHyperlinkResolverForOpenAPI3());
        BindingHandler.getHyperlinkResolver().setHyperlinkStyle("Hyperlink");

        // Defaults to ValueInserterPlainTextImpl()
        // BindingHandler.setValueInserterPlainText(new ValueInserterPlainTextForOpenAPI3());

        //Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE);
        //If a document doesn't include the Opendope definitions, eg. the XPathPart,
        //then the only thing you can do is insert the xml
        //the example document binding-simple.docx doesn't have an XPathPart....
        Docx4J.bind(wordMLPackage, xmlStream, Docx4J.FLAG_NONE);// | Docx4J.FLAG_BIND_REMOVE_SDT);

        // That won't invoke any XSLT Finisher (available in 6.1.0).  For that, you need to use signature:
        //  bind(WordprocessingMLPackage wmlPackage, Document xmlDocument, int flags, DocxFetcher docxFetcher,
        //  		XsltProvider xsltProvider, String xsltFinisherfilename, Map<String, Map<String, Object>> finisherParams)
        // Here is an example of using it on invoice.docx to shade a table row:
/*	    // Signature requires Document, not stream
  		Document xmlDoc = XmlUtils.getNewDocumentBuilder().parse(xmlStream);
  		// Specify the finished XSLT we want to us
	    Docx4jProperties.setProperty("docx4j.model.datastorage.XsltFinisher.xslt",  "XsltFinisherInvoice.xslt");
	    // Configure properties
	    Map<String, Map<String, Object>> finisherParams = new HashMap<String, Map<String, Object>>();
		Map<String, Object> tcPrParams = new HashMap<String, Object>();
		tcPrParams.put("fillColor", "5250D0"); // color to use for shading
		finisherParams.put("t_r",  tcPrParams);
		// Now perform the binding
		Docx4J.bind(wordMLPackage, xmlDoc, Docx4J.FLAG_BIND_INSERT_XML | Docx4J.FLAG_BIND_BIND_XML,
				null, new XsltProviderImpl(), null, finisherParams);
*/
        //Save the document
        Docx4J.save(wordMLPackage, new File(OUTPUT_DOCX), Docx4J.FLAG_NONE);
       /* Docx4J.toPDF(wordMLPackage,new FileOutputStream(OUTPUT_PDF));*/
        System.out.println("Saved: " + OUTPUT_DOCX);

    }
}
