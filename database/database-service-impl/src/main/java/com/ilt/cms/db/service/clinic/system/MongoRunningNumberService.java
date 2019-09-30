package com.ilt.cms.db.service.clinic.system;

import com.ilt.cms.core.entity.QueueStore;
import com.ilt.cms.core.entity.RunningNumber;
import com.ilt.cms.database.clinic.system.RunningNumberService;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.bson.Document;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Arrays;

@Service
public class MongoRunningNumberService implements RunningNumberService {

    private static final Logger logger = LoggerFactory.getLogger(MongoRunningNumberService.class);

    private static final String SALES_ORDER = "sales-order";
    private static final String PATIENT = "patient";
    private static final String BILL = "bill";
    private static final String VISIT = "visit";
    private static final String ADJUSTMENT = "adjustment";
    private static final String MEDICAL_CERTIFICATE = "medical-certificate";
    private static final String REQUEST = "request";
    private static final String ORDER = "order";
    private static final String GRN = "grn";
    private static final String GRVN = "grvn";
    private static final String DELIVERY_NOTE = "delivery-note";
    private static final String DELIVERY_VOID_NOTE = "delivery-void-note";
    private static final String INVOICE = "invoice";
    public static final String QUEUE = "queue";

    private final MongoTemplate mongoTemplate;

    public MongoRunningNumberService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @PostConstruct
    public void init() {
        logger.info("Initiating default runner number");
        for (String name : Arrays.asList(INVOICE, SALES_ORDER, PATIENT, BILL, VISIT, ADJUSTMENT, MEDICAL_CERTIFICATE, REQUEST,
                ORDER, GRN, GRVN, DELIVERY_NOTE, DELIVERY_VOID_NOTE)) {
            Query query = Query.query(Criteria.where("_id").is(name));
            if (!mongoTemplate.exists(query, RunningNumber.class)) {
                logger.info("Running number [" + name + "] does not exists creating new record");
                mongoTemplate.findAndModify(query,
                        new Update().set("runner", RunningNumber.MIN_NUMBER),
                        FindAndModifyOptions.options().upsert(true), RunningNumber.class);
            }
        }
        logger.info("initiation completed");
    }

    @Override
    public String generateSalesOrderNumber() {
        return getNextSequence(SALES_ORDER);
    }

    @Override
    public String generateInvoiceNumber() {
        return getNextSequence(INVOICE);
    }

    @Override
    public String generatePatientNumber() {
        return getNextSequence(PATIENT);
    }

    @Override
    public String generateBillNumber() {
        return getNextSequence(BILL);
    }

    @Override
    public String generateVisitNumber() {
        return getNextSequence(VISIT);
    }

    @Override
    public String generateAdjustmentNumber() {
        return getNextSequence(ADJUSTMENT);
    }

    @Override
    public String generateMedicalCertificateNumber() {
        return getNextSequence(MEDICAL_CERTIFICATE);
    }

    @Override
    public String generateRequestNumber() {
        return getNextSequence(REQUEST);
    }

    @Override
    public String generateOrderNumber() {
        return getNextSequence(ORDER);
    }

    @Override
    public String generateGRNNumber() {
        return getNextSequence(GRN);
    }

    @Override
    public String generateGRVNNumber() {
        return getNextSequence(GRVN);
    }

    @Override
    public String generateDeliveryNote() {
        return getNextSequence(DELIVERY_NOTE);
    }

    @Override
    public String generateDeliveryVoidNote() {
        return getNextSequence(DELIVERY_VOID_NOTE);
    }

    @Override
    public long queueNextNumber(String clinicCode, byte prefix) {

        String key = clinicCode + prefix;
        Query query = Query.query(Criteria.where("_id").is(key));

        QueueStore queueStore = mongoTemplate.findAndModify(query, new Update()
                        .setOnInsert("localDate", LocalDate.now())
                        .setOnInsert("queueNumber", 0L),
                FindAndModifyOptions.options().upsert(true).returnNew(true), QueueStore.class);

        if (!queueStore.getLocalDate().isEqual(LocalDate.now())) {
            mongoTemplate.updateFirst(Query.query(Criteria.where("_id").is(key).and("localDate").ne(LocalDate.now())), new Update()
                    .set("localDate", LocalDate.now())
                    .set("queueNumber", 0L), QueueStore.class);
        }
        queueStore = mongoTemplate.findAndModify(query, new Update().inc("queueNumber", 1),
                FindAndModifyOptions.options().returnNew(true), QueueStore.class);

        return Long.valueOf(prefix + "0" + queueStore.getQueueNumber());
    }


    private String getNextSequence(String name) {

        long runningNumber = newRunningNumber(name);
        if (runningNumber >= RunningNumber.MAX_NUMBER) {
            long restNumber = resetCounterToMinimum(name);
            logger.info("Number reset to [" + restNumber + "]");
            if (restNumber == RunningNumber.MIN_NUMBER || restNumber == 0) {
                logger.info("Regenerated number [" + runningNumber + "]");
            }
        }

        LocalDate localDate = LocalDate.now();
        return localDate.getYear() + "" + localDate.getDayOfYear() + "" + runningNumber;
    }

    private long newRunningNumber(String name) {
        RunningNumber runner = mongoTemplate.findAndModify(Query.query(Criteria.where("_id").is(name)),
                new Update().inc("runner", 1), FindAndModifyOptions.options().returnNew(true),
                RunningNumber.class);

        long runningNumber = 0;
        if (runner != null) {
            runningNumber = runner.getRunner();
        }
        return runningNumber;
    }

    /**
     * This is done using native mongo so that incase something needs to be changd we can still change it
     * Concurrency of this need to be tested
     *
     * @param name
     * @return
     */
    private long resetCounterToMinimum(String name) {
        MongoCollection<Document> collection = mongoTemplate.getCollection(mongoTemplate.getCollectionName(RunningNumber.class));
        BasicDBList value = new BasicDBList();
        value.add(new BasicDBObject("runner", new BasicDBObject("$gte", RunningNumber.MAX_NUMBER)));
        BasicDBObject searchQuery = new BasicDBObject("_id", name).append("$and", value);

        BasicDBObject updateQuery = new BasicDBObject("runner", RunningNumber.MIN_NUMBER);
        FindOneAndUpdateOptions findOneAndUpdateOptions = new FindOneAndUpdateOptions();
        findOneAndUpdateOptions.upsert(true);
        findOneAndUpdateOptions.returnDocument(ReturnDocument.AFTER);
        Document result = collection.findOneAndUpdate(searchQuery, updateQuery, findOneAndUpdateOptions);
        if (result != null) {
            logger.info("Max runner reached for name[" + name + "] resetting value");
            return Long.valueOf(result.get("runner").toString());
        } else {
            return 0;
        }
    }
}
