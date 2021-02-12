package nitpicksy.literarysociety.elasticsearch.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

@Service
public class ApacheTikaHandler {

    private static final Parser PARSERS[] = new Parser[] {
            // documents
            new org.apache.tika.parser.html.HtmlParser(),
            new org.apache.tika.parser.rtf.RTFParser(),
            new org.apache.tika.parser.pdf.PDFParser(),
            new org.apache.tika.parser.txt.TXTParser(),
            new org.apache.tika.parser.microsoft.OfficeParser(),
            new org.apache.tika.parser.microsoft.OldExcelParser(),
            new org.apache.tika.parser.microsoft.ooxml.OOXMLParser(),
            new org.apache.tika.parser.odf.OpenDocumentParser(),
            new org.apache.tika.parser.iwork.IWorkPackageParser(),
            new org.apache.tika.parser.xml.DcXMLParser(),
    };

    public  String extractContentUsingParser(InputStream stream) {

        Parser parser = new AutoDetectParser();
        ContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        try {
            parser.parse(stream, handler, metadata, context);
        } catch (IOException | SAXException | TikaException e) {
            return null;
        }
        return handler.toString();
    }
}
