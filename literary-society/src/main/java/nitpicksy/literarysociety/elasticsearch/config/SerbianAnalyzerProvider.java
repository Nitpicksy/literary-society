package nitpicksy.literarysociety.elasticsearch.config;

import java.io.IOException;

import nitpicksy.literarysociety.elasticsearch.plugin.SerbianAnalyzer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.IndexSettings;
import org.elasticsearch.index.analysis.AbstractIndexAnalyzerProvider;

/**
 * To set Analyzer on indexed entity field add annotation
 * @Field ( type = FieldType.String, index = FieldIndex.analyzed, store = true, analyzer="serbian_analyzer")
 */
@SuppressWarnings("rawtypes")
public class SerbianAnalyzerProvider extends AbstractIndexAnalyzerProvider{

    public static final String NAME = "serbian_analyzer";
    private final SerbianAnalyzer analyzer;

    public SerbianAnalyzerProvider(IndexSettings indexSettings, String name, Settings settings) throws IOException {
        super(indexSettings, name, settings);
        analyzer = new SerbianAnalyzer();
        analyzer.setVersion(version);
    }

    @Override
    public SerbianAnalyzer get(){
        return analyzer;
    }

}


