package org.example.timer;

import org.example.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
/**
 * A component class that schedules the deletion of requests older than one week.
 */
@Component
public class RequestTimer {

    private final RequestService requestService;
    private final Logger logger = LoggerFactory.getLogger(RequestTimer.class);


    public RequestTimer(RequestService requestService) {
        this.requestService = requestService;
    }
    /**
     * Deletes all requests that are older than one week.
     * This method is scheduled to run with a fixed delay of 1 day.
     */
    @Scheduled(fixedDelay = 1, timeUnit = TimeUnit.DAYS)
    public void deleteAllRequestsByWeek() {
        logger.warn("Requests for the past 7 days have been deleted");
        requestService.deleteRequestsByWeek();
    }
}
