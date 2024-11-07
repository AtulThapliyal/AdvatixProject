package com.employee.advatixAPI.service.carrier;

import com.employee.advatixAPI.dto.carrier.Event;
import com.employee.advatixAPI.dto.carrier.TrackingNumber;
import com.employee.advatixAPI.dto.carrier.TrackingRequest;
import com.employee.advatixAPI.dto.carrier.TrackingResponse;
import com.employee.advatixAPI.entity.carrier.ShipmentJourney;
import com.employee.advatixAPI.entity.carrier.ThirdPartyStatus;
import com.employee.advatixAPI.repository.carrier.ShipmentJourneyRepository;
import com.employee.advatixAPI.repository.carrier.ThirdPartyStatusRepository;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class TrackingShipmentService {

    @Autowired
    ThirdPartyStatusRepository statusRepository;

    @Autowired
    ShipmentJourneyRepository shipmentJourneyRepository;

    @Value("${TRK.BASEURL.VALUE}")
    private String baseUrl;

    @Value("${TRK.APIKEY.VALUE}")
    private String apiKey;

    public TrackingResponse callShipmentApi(TrackingRequest trackingRequest) throws URISyntaxException {
        URI uri = new URI(baseUrl);
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("content-type", "application/json");
        headers.set("api-key", apiKey);

        HttpEntity<TrackingRequest> trackingRequestHttpEntity = new HttpEntity<>(trackingRequest, headers);

        ResponseEntity<TrackingResponse> result = restTemplate.postForEntity(uri, trackingRequestHttpEntity, TrackingResponse.class);

        return result.getBody();
    }
//
    public String createPdf(String dest, List<String> urls) throws IOException, DocumentException {

        Image img = Image.getInstance(urls.get(0));

        Document document = new Document(img);
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        for (String image : urls) {
            img = Image.getInstance(image);
            document.setPageSize(PageSize.A4);
            document.newPage();
            img.setAbsolutePosition(0, 0);
            document.add(img);
        }
        document.close();

        return dest;
    }

    public String getTrackingResponse(TrackingRequest trackingRequest) {
        String filePath = "";

        try {
            TrackingResponse response = callShipmentApi(trackingRequest);
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");

            for (TrackingNumber trackedNumber : response.getPayload().getTrackingNumbers()) {
                filePath = handleTrackingNumber(trackedNumber, targetFormat, originalFormat);
            }

            return "Done";
        } catch (Exception e) {
            return "Nothing found";
        }
    }

    private String handleTrackingNumber(TrackingNumber trackedNumber, SimpleDateFormat targetFormat, SimpleDateFormat originalFormat) throws IOException, DocumentException {
        String filePath = "";

        if (trackedNumber.getPods() != null) {
            filePath = createPdf("C:\\Users\\yogia\\Desktop\\Advatix Project\\AdvatixProject\\Photos\\" + trackedNumber.getTrackingNumber() + ".pdf", trackedNumber.getPods());
        }
        String finalFilePath = filePath;

        trackedNumber.getEvents().forEach(event -> {
            ThirdPartyStatus thirdPartyStatus = createStatus(event, trackedNumber, finalFilePath, targetFormat, originalFormat);
            statusRepository.save(thirdPartyStatus);

            if (!shipmentJourneyRepository.findByStatusAndTime(event.getEventDescription(), thirdPartyStatus.getTime()).isPresent()) {
                ShipmentJourney shipmentJourney = createShipmentJourney(event, trackedNumber, finalFilePath, targetFormat, originalFormat);
                shipmentJourneyRepository.save(shipmentJourney);
            }
        });

        return filePath;
    }

    private ThirdPartyStatus createStatus(Event event, TrackingNumber trackedNumber, String filePath, SimpleDateFormat targetFormat, SimpleDateFormat originalFormat) {
        ThirdPartyStatus thirdPartyStatus = new ThirdPartyStatus();
        thirdPartyStatus.setStatus(event.getEventDescription());
        thirdPartyStatus.setBarCode(event.getBarCode());

        if (trackedNumber.getPods() != null) {
            thirdPartyStatus.setImagePath(filePath);
        }

        try {
            thirdPartyStatus.setTime(formatTimestamp(event.getTs(), targetFormat, originalFormat));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return thirdPartyStatus;
    }

    private ShipmentJourney createShipmentJourney(Event event, TrackingNumber trackedNumber, String filePath, SimpleDateFormat targetFormat, SimpleDateFormat originalFormat) {
        ShipmentJourney shipmentJourney = new ShipmentJourney();
        shipmentJourney.setStatus(event.getEventDescription());
        shipmentJourney.setBarCode(event.getBarCode());
        shipmentJourney.setWebHookSent(true);

        if (trackedNumber.getPods() != null) {
            shipmentJourney.setImagePath(filePath);
        }

        try {
            shipmentJourney.setTime(formatTimestamp(event.getTs(), targetFormat, originalFormat));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        return shipmentJourney;
    }

    private String formatTimestamp(String timestamp, SimpleDateFormat targetFormat, SimpleDateFormat originalFormat) throws ParseException {
        return targetFormat.format(originalFormat.parse(timestamp));
    }
}