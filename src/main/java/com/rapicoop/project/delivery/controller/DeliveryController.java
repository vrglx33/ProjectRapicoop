package com.rapicoop.project.delivery.controller;

import com.rapicoop.project.delivery.model.Delivery;
import com.rapicoop.project.delivery.model.Report;
import com.rapicoop.project.delivery.repository.DeliveryRepository;
import com.rapicoop.project.distributor.exception.ResourceNotFoundException;
import com.rapicoop.project.distributor.model.Distributor;
import com.rapicoop.project.distributor.repository.DistributorRepository;
import com.rapicoop.project.status.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/api")
public class DeliveryController {
    @Autowired
    DeliveryRepository deliveryRepository;
    @Autowired
    DistributorRepository distributorRepository;

    private static final double INITIAL_LATITUDE = 4.632502;
    private static final double INITIAL_LONGITUDE = -74.065344;

    /**
     *  Get All deliveries
     * @return deliveries Array
     */
    @GetMapping("/deliveries")
    public List<Delivery> getAllDeliveries() {
        return deliveryRepository.findAll();
    }

    /**
     *  Get the last delivery from a distributor
     * @return last delivery
     */
    @GetMapping("/deliveries/last/distributor/{id}")
    public Delivery getAllLastDeliveryByDistributor(@PathVariable(value = "id") Long distributorId) {
        Distributor distributor;
        distributor= distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
        Delivery [] delivers = distributor.getDelivery().toArray(new Delivery[0]);
        if(delivers.length < 1){
            throw new ResourceNotFoundException("Delivery", "id", distributorId);
        }
        return delivers[0];
    }

    /**
     *  Get the all deliveries from a distributor
     * @return last delivery
     */
    @GetMapping("/deliveries/all/distributor/{id}")
    public Set<Delivery> getAllDeliveriesByDistributor(@PathVariable(value = "id") Long distributorId) {
        Distributor distributor;
        distributor= distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
        Delivery [] delivers = distributor.getDelivery().toArray(new Delivery[0]);
        if(delivers.length < 1){
            throw new ResourceNotFoundException("Delivery", "id", distributorId);
        }
        return distributor.getDelivery();
    }

    /**
     *  Get the all deliveries from a distributor in an time interval
     * @return last delivery
     */
    @GetMapping("/deliveries/all/distributor/{id}/from/{dateFrom}/to/{dateTo}")
    public ArrayList<Delivery> getAllDeliveriesIntervalByDistributor(@PathVariable(value = "id") Long distributorId,
                                                               @PathVariable(value = "dateFrom") String dateFrom,
                                                               @PathVariable(value = "dateTo") String dateTo) throws ParseException {
        Distributor distributor;
        distributor= distributorRepository.findById(distributorId)
                .orElseThrow(() -> new ResourceNotFoundException("Distributor", "id", distributorId));
        Delivery [] delivers = distributor.getDelivery().toArray(new Delivery[0]);
        ArrayList<Delivery> response = new ArrayList<Delivery>();
        if(delivers.length < 1){
            throw new ResourceNotFoundException("Delivery", "id", distributorId);
        }else{
            for (Delivery deliver : delivers) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                Date from = df.parse(dateFrom);
                Date to = df.parse(dateTo);
                Date deliverDate = df.parse(String.valueOf(deliver.getModifiedAt()));
                if((deliverDate.after(from) && deliverDate.before(to)) ||
                        deliverDate.equals(to) ||
                        deliverDate.equals(from)) {
                    response.add(deliver);
                }
            }
        }
        return response;
    }


    /**
     *  Get the average from all of the deliveries
     * @return last delivery
     */
    @GetMapping("/deliveries/average/report")
    public Report getAverageReport() {
        ArrayList<Delivery> deliveries = (ArrayList<Delivery>) deliveryRepository.findAll();
        Report report = new Report();
        int index = 0;
        double summatoryDistance = 0;
        double summatoryTime = 0;
        double summatorySucceded = 0;
        double summatoryFailed = 0;
        for(Delivery delivery : deliveries) {
            double lat = Double.parseDouble(delivery.getLatitude());
            double longitude = Double.parseDouble(delivery.getLongitude());
            summatoryDistance += calculateDistance( lat, longitude, INITIAL_LATITUDE, INITIAL_LONGITUDE,0,0);
            long diff = delivery.getModifiedAt().getTime() - delivery.getCreatedAt().getTime();
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            summatorySucceded = delivery.getStatus() == 2 ? summatorySucceded + 1 : summatorySucceded;
            summatoryFailed = (delivery.getStatus() == 0 || delivery.getStatus() == 1) ? summatoryFailed + 1 : summatoryFailed;
            summatoryTime += minutes;
            index++;
        }
        report.failedDeliveries = summatoryFailed;
        report.succededDeliveries = summatorySucceded;
        report.averageTime = summatoryTime/ (index == 0 ? 1 : index);
        report.averageDistance = summatoryDistance/ (index == 0 ? 1 : index);
        return report;
    }


    /**
     * Calculate distance between two points in latitude and longitude taking
     * into account height difference. If you are not interested in height
     * difference pass 0.0. Uses Haversine method as its base.
     *
     * lat1, lon1 Start point lat2, lon2 End point el1 Start altitude in meters
     * el2 End altitude in meters
     * @returns Distance in Meters
     */
    public static double calculateDistance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /**
     * Create a new delivery
     * @param delivery
     * @return created delivery
     */
    @PostMapping("/delivery")
    public Delivery createDelivery(@Valid @RequestBody Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    /**
     * Get a single delivery
     * @param deliveryId
     * @return delivery object
     */
    @GetMapping("/delivery/{id}")
    public Delivery getDeliveryById(@PathVariable(value = "id") Long deliveryId) {
        return deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "id", deliveryId));
    }

    /**
     * Update the Delivery Status
     * @param deliveryId
     * @param distributorId
     * @param statusId
     * @return response or error
     */
    @GetMapping("/delivery/{id}/distributor/{distributorid}/status/{statusid}")
    public Delivery updateDelivery(@PathVariable(value = "id") Long deliveryId,
                                   @PathVariable(value = "distributorid") Long distributorId,
                                   @PathVariable(value = "statusid") Integer statusId
                                   ) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("Delivery", "id", deliveryId));
        if(delivery.getDistributor().getId() == distributorId) {
            delivery.setStatus(statusId);
        }
        return delivery;
    }

    /**
     * Deletes a delivery
     * @param deliveryId
     * @return deleted delivery or not found
     */
    @DeleteMapping("/delivery/{id}")
    public ResponseEntity<?> deleteDelivery(@PathVariable(value = "id") Long deliveryId) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new ResourceNotFoundException("History", "id", deliveryId));
        deliveryRepository.delete(delivery);
        return ResponseEntity.ok().build();
    }
}
