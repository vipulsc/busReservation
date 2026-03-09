package com.bus.cleaning.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bus.cleaning.config.AppConfig;
import com.bus.cleaning.model.Booking;
import com.bus.cleaning.repository.AggregationDbRepository;
import com.bus.cleaning.repository.BookingReader;
import com.bus.cleaning.repository.BookingWriter;
import com.bus.cleaning.repository.CsvBookingReader;
import com.bus.cleaning.repository.CsvBookingWriter;
import com.bus.cleaning.repository.DbBookingRepository;
import com.bus.cleaning.rules.CodeMappingRule;
import com.bus.cleaning.rules.DateStandardizationRule;
import com.bus.cleaning.rules.DerivedFieldsRule;
import com.bus.cleaning.rules.NameNormalizationRule;
import com.bus.cleaning.rules.NumericValidationRule;
import com.bus.cleaning.rules.StatusNormalizationRule;
import com.bus.cleaning.service.AggregationService;
import com.bus.cleaning.service.CleaningPipeline;
import com.bus.cleaning.service.DuplicateService;

public class Application {

    private static final Logger logger =
            LogManager.getLogger(Application.class);

    public static void main(String[] args) throws Exception {

        AppConfig cfg = AppConfig.load();
        logger.info("==================================");
        logger.info("BUS CLEANING JOB STARTED");
        logger.info("==================================");


        // STEP 1: READ RAW DATA
        BookingReader reader = new CsvBookingReader();
        BookingWriter writer = new CsvBookingWriter();

        List<Booking> all = reader.readAll(cfg.inputPath);
        logger.info("Total records read: {}", all.size());

        // STEP 2: CLEANING PIPELINE
        CleaningPipeline pipeline = new CleaningPipeline(Arrays.asList(
                new NameNormalizationRule(),
                new NumericValidationRule(),
                new DateStandardizationRule(),
                new StatusNormalizationRule(),
                new CodeMappingRule(),
                new DerivedFieldsRule()
        ));

        List<Booking> valid = new ArrayList<>();
        List<Booking> invalid = new ArrayList<>();

        for (Booking b : all) {
            pipeline.process(b);
            if (b.valid)
                valid.add(b);
            else
                invalid.add(b);
        }

        logger.info("Valid before de-dup: {}", valid.size());
        logger.info("Rejected records: {}", invalid.size());

        // STEP 3: REMOVE DUPLICATES
        DuplicateService dup = new DuplicateService();
        List<Booking> uniqueValid = dup.removeDuplicates(valid);

        logger.info("Valid after de-dup: {}", uniqueValid.size());
     
        // STEP 4: SAVE TO DATABASE
        if (cfg.dbEnabled) {

            logger.info("DB ENABLED - Saving records");

            // Booking table
            DbBookingRepository bookingRepo =
                    new DbBookingRepository();

            bookingRepo.saveAll(cfg, uniqueValid);

            // Aggregation
            AggregationService aggService =
                    new AggregationService();

            List<AggregationService.AggregationRow> rows =
                    aggService.routeWiseSummary(uniqueValid);

            AggregationDbRepository aggRepo =
                    new AggregationDbRepository();

            aggRepo.saveAll(cfg, rows);

            logger.info("Database operations completed successfully");
        }

      
        // STEP 5: WRITE OUTPUT FILES
        writer.writeCleaned(cfg.cleanedPath, uniqueValid);
        writer.writeRejected(cfg.rejectedPath, invalid);

        // Aggregation CSV
        AggregationService aggService =
                new AggregationService();

        List<AggregationService.AggregationRow> rows =
                aggService.routeWiseSummary(uniqueValid);

        writer.writeAggregation(cfg.aggregationPath, rows);

        logger.info("Files generated:");
        logger.info(" - {}", cfg.cleanedPath);
        logger.info(" - {}", cfg.rejectedPath);
        logger.info(" - {}", cfg.aggregationPath);

        logger.info("==================================");
        logger.info("BUS CLEANING JOB COMPLETED SUCCESSFULLY");
        logger.info("==================================");
    }
}