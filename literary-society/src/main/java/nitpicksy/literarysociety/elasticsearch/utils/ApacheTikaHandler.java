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
        return handler.toString().replace("\n", " ");
    }
}
