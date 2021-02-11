package nitpicksy.literarysociety.utils;

import com.byteowls.jopencage.JOpenCageGeocoder;
import com.byteowls.jopencage.model.JOpenCageForwardRequest;
import com.byteowls.jopencage.model.JOpenCageLatLng;
import com.byteowls.jopencage.model.JOpenCageResponse;
import com.byteowls.jopencage.model.JOpenCageResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationProvider {

    public static JOpenCageLatLng getCoordinates(String location) {
        JOpenCageGeocoder jOpenCageGeocoder = new JOpenCageGeocoder("902161f9b3c845f58629e7d3593dd297");
        JOpenCageForwardRequest request = new JOpenCageForwardRequest(location);
        JOpenCageResponse response = jOpenCageGeocoder.forward(request);
        List<JOpenCageResult> resultList = response.getResults();
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0).getGeometry();
        }

        return null;
    }

}
