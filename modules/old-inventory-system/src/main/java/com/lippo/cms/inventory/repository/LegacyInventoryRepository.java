package com.lippo.cms.inventory.repository;

import com.lippo.cms.inventory.model.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

@Repository
public class LegacyInventoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(LegacyInventoryRepository.class);

    final private JdbcTemplate jdbcTemplate;
    final private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${inventory.patient.id}")
    private int patientId;

    @Value("${inventory.patient.register.detail.isCostCalculated}")
    private boolean isCostCalculated;

    public LegacyInventoryRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    private InventoryDetail mapRsToInventoryDetail(ResultSet rs) throws SQLException {
        return new InventoryDetail(
            rs.getInt("InventoryDetailID"),
            rs.getInt("InventoryTbl_ID"),
            rs.getInt("ClinicTbl_ID"),
            rs.getInt("CodeListing_ChargeTbl_ID"),
            rs.getDate("ExpiryDate"),
            rs.getString("LotNo"),
            rs.getDouble("Qty"),
            rs.getDouble("QtyTotal"),
            rs.getDouble("Amount"),
            rs.getDouble("ItemAmount"),
            rs.getDouble("ItemCost"),
            rs.getDouble("RemainingQty"),
            rs.getDouble("RemainingAmount"),
            rs.getDate("DateCreated"),
            rs.getInt("UserTbl_CreatedUserID"),
            rs.getDate("DateUpdated"),
            rs.getInt("UserTbl_UpdatedUserID"),
            rs.getString("Code"),
            rs.getDouble("TotalQty"),
            rs.getDouble("TotalAmount"),
            rs.getDouble("Cost"),
            rs.getBoolean("MixingFlag")
        );
    }

    private DrugDispensing mapRsToDrugDispensing(ResultSet rs) throws SQLException {
        return new DrugDispensing(
            rs.getInt("DrugDispensingID"),
            rs.getInt("PatientRegisterDetailTbl_ID"),
            rs.getInt("CodeListing_ChargeTbl_ID"),
            rs.getInt("ClinicTbl_ID"),
            rs.getInt("InventoryDetailTbl_ID"),
            rs.getDouble("Qty"),
            rs.getDouble("StockAmount"),
            rs.getDate("DateCreated"),
            rs.getInt("UserTbl_CreatedUserID"),
            rs.getDate("DateUpdated"),
            rs.getInt("UserTbl_UpdatedUserID")
        );
    }

    private DrugInventory mapRsToDrugInventory(ResultSet rs) throws SQLException {
        return new DrugInventory(
            rs.getInt("ID"),
            rs.getInt("CodeListing_ChargeTbl_ID"),
            rs.getInt("ClinicTbl_ID"),
            rs.getDouble("CurrentTotal"),
            rs.getDouble("CurrentAmount"),
            rs.getDouble("TotalQty"),
            rs.getDouble("TotalAmount"),
            rs.getDate("DateCreated"),
            rs.getInt("UserTbl_CreatedUserID"),
            rs.getDate("DateUpdated"),
            rs.getInt("UserTbl_UpdatedUserID")
        );
    }

    private PatientDrug mapRsToPatientDrug(ResultSet rs) throws SQLException {
        return new PatientDrug(
            rs.getInt("ID"),
            rs.getInt("PatientRegisterDetailTbl_ID"),
            rs.getInt("CodeListing_ChargeTbl_ID"),
            rs.getDouble("Qty"),
            rs.getDouble("Cost"),
            rs.getDate("DateCreated"),
            rs.getInt("UserTbl_CreatedUserID"),
            rs.getDate("DateUpdated"),
            rs.getInt("UserTbl_UpdatedUserID")
        );
    }

    private PatientRegister mapRsToPatientRegister(ResultSet rs) throws SQLException {
        return new PatientRegister(
            rs.getInt("PatientRegisterID"),
            rs.getInt("PatientTbl_ID"),
            rs.getInt("ClinicTbl_ID"),
            rs.getDate("DateIn"),
            rs.getString("PatientStatus"),
            rs.getDate("DateCreated"),
            rs.getInt("UserTbl_CreatedUserID"),
            rs.getDate("DateUpdated"),
            rs.getInt("UserTbl_UpdatedUserID")
        );
    }

    public List<InventoryDetail> getInventoryDetailList(String clinicCode, List<InventoryUsage> inventoryUsages) {
        List<InventoryDetail> selectedInventoryDetailList = new ArrayList<>();
        if (inventoryUsages.size() == 0) {
            return selectedInventoryDetailList;
        }
        List<String> drugCodes = inventoryUsages.stream().map(InventoryUsage::getItemCode).collect(Collectors.toList());
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clinicCode", clinicCode);
        mapSqlParameterSource.addValue("drugCodes", drugCodes);
        List<InventoryDetail> inventoryDetailListPerClinic = namedParameterJdbcTemplate.query(
        "SELECT inventory.*, charge.Code, drug.TotalQty, drug.TotalAmount, (case when drug.TotalQty != 0 then (drug.TotalAmount / drug.TotalQty) else drug.NettPrice end) as Cost, drugCode.MixingFlag FROM inventorydetailtbl inventory " +
            "LEFT JOIN clinictbl clinic ON clinic.ClinicID = inventory.ClinicTbl_ID " +
            "LEFT JOIN codelisting_chargetbl charge ON (charge.CodeListing_ChargeID = inventory.CodeListing_ChargeTbl_ID and charge.Disabled = 0 and charge.DeletedFlag = 0) " +
            "LEFT JOIN druginventorytbl drug ON (drug.CodeListing_ChargeTbl_ID = inventory.CodeListing_ChargeTbl_ID and drug.ClinicTbl_ID = inventory.ClinicTbl_ID and drug.DeletedFlag = 0) " +
            "LEFT JOIN codelisting_drugtbl drugCode on (drugCode.CodeListing_ChargeTbl_ID = drug.CodeListing_ChargeTbl_ID and drugCode.DeletedFlag = 0) " +
            "WHERE clinic.Code = :clinicCode and charge.Code IN (:drugCodes) and (inventory.ExpiryDate >= GETDATE() or inventory.ExpiryDate is null) " +
            "ORDER BY (case when inventory.LotNo is null then 1 else 0 end), (case when inventory.ExpiryDate is null then 1 else 0 end), inventory.RemainingQty desc, inventory.DateCreated",
            mapSqlParameterSource,
            (rs, rowNum) -> mapRsToInventoryDetail(rs)
        );
        for (InventoryUsage inventoryUsage: inventoryUsages) {
            List<InventoryDetail> inventoryDetailListPerClinicPerDrug = inventoryDetailListPerClinic.stream()
                .filter(detail -> detail.getDrugCode().equals(inventoryUsage.getItemCode())).collect(Collectors.toList());
            if (inventoryDetailListPerClinicPerDrug.size() > 0) {
                for (InventoryDetail inventoryDetail: inventoryDetailListPerClinicPerDrug) {
                    if (inventoryDetail.getRemainingQuantity() >= inventoryUsage.getQuantity()) {
                        inventoryDetail.setInventoryUsageIndex(inventoryUsage.getIndex());
                        selectedInventoryDetailList.add(inventoryDetail);
                        break;
                    }
                }
            }
        }
        return selectedInventoryDetailList;
    }

    public List<InventoryDetail> getAllInventoryDetailList(String clinicCode, List<InventoryUsage> inventoryUsages) {
        List<InventoryDetail> selectedInventoryDetailList = new ArrayList<>();
        if (inventoryUsages.size() == 0) {
            return selectedInventoryDetailList;
        }
        List<String> drugCodes = inventoryUsages.stream().map(InventoryUsage::getItemCode).collect(Collectors.toList());
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clinicCode", clinicCode);
        mapSqlParameterSource.addValue("drugCodes", drugCodes);
        List<InventoryDetail> inventoryDetailListPerClinic = namedParameterJdbcTemplate.query(
                "SELECT inventory.*, charge.Code, drug.TotalQty, drug.TotalAmount, (case when drug.TotalQty != 0 then (drug.TotalAmount / drug.TotalQty) else drug.NettPrice end) as Cost, drugCode.MixingFlag FROM inventorydetailtbl inventory " +
                        "LEFT JOIN clinictbl clinic ON clinic.ClinicID = inventory.ClinicTbl_ID " +
                        "LEFT JOIN codelisting_chargetbl charge ON (charge.CodeListing_ChargeID = inventory.CodeListing_ChargeTbl_ID and charge.Disabled = 0 and charge.DeletedFlag = 0) " +
                        "LEFT JOIN druginventorytbl drug ON (drug.CodeListing_ChargeTbl_ID = inventory.CodeListing_ChargeTbl_ID and drug.ClinicTbl_ID = inventory.ClinicTbl_ID and drug.DeletedFlag = 0) " +
                        "LEFT JOIN codelisting_drugtbl drugCode on (drugCode.CodeListing_ChargeTbl_ID = drug.CodeListing_ChargeTbl_ID and drugCode.DeletedFlag = 0) " +
                        "WHERE clinic.Code = :clinicCode and charge.Code IN (:drugCodes) and (inventory.ExpiryDate >= GETDATE() or inventory.ExpiryDate is null) " +
                        "ORDER BY (case when inventory.LotNo is null then 1 else 0 end), (case when inventory.ExpiryDate is null then 1 else 0 end), inventory.RemainingQty desc, inventory.DateCreated",
                mapSqlParameterSource,
                (rs, rowNum) -> mapRsToInventoryDetail(rs)
        );
        for (InventoryUsage inventoryUsage: inventoryUsages) {
            List<InventoryDetail> inventoryDetailListPerClinicPerDrug = inventoryDetailListPerClinic.stream()
                    .filter(detail -> detail.getDrugCode().equals(inventoryUsage.getItemCode())).collect(Collectors.toList());
            if (inventoryDetailListPerClinicPerDrug.size() > 0) {
                for (InventoryDetail inventoryDetail: inventoryDetailListPerClinicPerDrug) {
                    if (inventoryDetail.getRemainingQuantity() >= inventoryUsage.getQuantity()) {
                        inventoryDetail.setInventoryUsageIndex(inventoryUsage.getIndex());
                        selectedInventoryDetailList.add(inventoryDetail);
                    }
                }
            }
        }
        return selectedInventoryDetailList;
    }

    public List<InventoryDetail> getInventoryDetailListWithBatchNo(String clinicCode, List<InventoryUsage> inventoryUsages) {
        List<InventoryDetail> selectedInventoryDetailList = new ArrayList<>();
        if (inventoryUsages.size() == 0) {
            return selectedInventoryDetailList;
        }
        List<InventoryUsage> aggregatedInventoryUsages = new ArrayList<>();
        Map<String, Double> inventoryUsageMap = inventoryUsages.stream().collect(Collectors.groupingBy(InventoryUsage::getBatchNo, Collectors.summingDouble(InventoryUsage::getQuantity)));
        for (String batchNo: inventoryUsageMap.keySet()) {
            InventoryUsage inventoryUsage = inventoryUsages.stream().filter(inventoryUsageItem -> inventoryUsageItem.getBatchNo().equals(batchNo)).findFirst().get();
            inventoryUsage.setQuantity(inventoryUsageMap.get(batchNo));
            aggregatedInventoryUsages.add(inventoryUsage);
        }
        logger.debug("inventoryUsageMap: " + inventoryUsageMap + ", aggregatedInventoryUsages" + aggregatedInventoryUsages);
        List<String> drugCodes = aggregatedInventoryUsages.stream().map(InventoryUsage::getItemCode).collect(Collectors.toList());
        List<String> batchNumbers = aggregatedInventoryUsages.stream().map(InventoryUsage::getBatchNo).collect(Collectors.toList());
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clinicCode", clinicCode);
        mapSqlParameterSource.addValue("drugCodes", drugCodes);
        mapSqlParameterSource.addValue("batchNumbers", batchNumbers);
        List<InventoryDetail> inventoryDetailListPerClinic = namedParameterJdbcTemplate.query(
            "SELECT inventory.*, charge.Code, drug.TotalQty, drug.TotalAmount, (case when drug.TotalQty != 0 then (drug.TotalAmount / drug.TotalQty) else drug.NettPrice end) as Cost, drugCode.MixingFlag FROM inventorydetailtbl inventory " +
            "LEFT JOIN clinictbl clinic ON clinic.ClinicID = inventory.ClinicTbl_ID " +
            "LEFT JOIN codelisting_chargetbl charge ON (charge.CodeListing_ChargeID = inventory.CodeListing_ChargeTbl_ID and charge.Disabled = 0 and charge.DeletedFlag = 0) " +
            "LEFT JOIN druginventorytbl drug ON (drug.CodeListing_ChargeTbl_ID = inventory.CodeListing_ChargeTbl_ID and drug.ClinicTbl_ID = inventory.ClinicTbl_ID and drug.DeletedFlag = 0) " +
            "LEFT JOIN codelisting_drugtbl drugCode on (drugCode.CodeListing_ChargeTbl_ID = drug.CodeListing_ChargeTbl_ID and drugCode.DeletedFlag = 0) " +
            "WHERE clinic.Code = :clinicCode AND charge.Code IN (:drugCodes) " +
            "AND (inventory.LotNo IN (:batchNumbers) or inventory.LotNo is null) " +
            "AND (inventory.ExpiryDate >= GETDATE() or inventory.ExpiryDate is null) " +
            "ORDER BY (case when inventory.LotNo is null then 1 else 0 end), (case when inventory.ExpiryDate is null then 1 else 0 end), inventory.RemainingQty desc, inventory.DateCreated",
            mapSqlParameterSource,
            (rs, rowNum) -> mapRsToInventoryDetail(rs)
        );
        logger.debug("inventoryDetailListPerClinic: " + inventoryDetailListPerClinic);
        for (InventoryUsage inventoryUsage: aggregatedInventoryUsages) {
            List<InventoryDetail> inventoryDetailListPerClinicPerDrug = inventoryDetailListPerClinic.stream()
                .filter(detail -> detail.getDrugCode().equals(inventoryUsage.getItemCode()))
                .filter(detail -> detail.getBatchNo() == null || detail.getBatchNo().equals("") || detail.getBatchNo().equals(inventoryUsage.getBatchNo()))
                .collect(Collectors.toList());
            if (inventoryDetailListPerClinicPerDrug.size() > 0) {
                for (InventoryDetail inventoryDetail: inventoryDetailListPerClinicPerDrug) {
                    logger.debug("inventoryDetail: " + inventoryDetail + ", inventoryUsage: " + inventoryUsage);
                    if (inventoryDetail.getRemainingQuantity() >= inventoryUsage.getQuantity()) {
                        inventoryDetail.setInventoryUsageIndex(inventoryUsage.getIndex());
                        selectedInventoryDetailList.add(inventoryDetail);
                        break;
                    }
                }
            }
        }
        return selectedInventoryDetailList;
    }

    public List<InventoryDetail> getAllInventoryDetailListWithBatchNo(String clinicCode, List<InventoryUsage> inventoryUsages) {
        List<InventoryDetail> selectedInventoryDetailList = new ArrayList<>();
        if (inventoryUsages.size() == 0) {
            return selectedInventoryDetailList;
        }
        List<InventoryUsage> aggregatedInventoryUsages = new ArrayList<>();
        Map<String, Double> inventoryUsageMap = inventoryUsages.stream().collect(Collectors.groupingBy(InventoryUsage::getBatchNo, Collectors.summingDouble(InventoryUsage::getQuantity)));
        for (String batchNo: inventoryUsageMap.keySet()) {
            InventoryUsage inventoryUsage = inventoryUsages.stream().filter(inventoryUsageItem -> inventoryUsageItem.getBatchNo().equals(batchNo)).findFirst().get();
            inventoryUsage.setQuantity(inventoryUsageMap.get(batchNo));
            aggregatedInventoryUsages.add(inventoryUsage);
        }
        logger.debug("inventoryUsageMap: " + inventoryUsageMap + ", aggregatedInventoryUsages" + aggregatedInventoryUsages);
        List<String> drugCodes = aggregatedInventoryUsages.stream().map(InventoryUsage::getItemCode).collect(Collectors.toList());
        List<String> batchNumbers = aggregatedInventoryUsages.stream().map(InventoryUsage::getBatchNo).collect(Collectors.toList());
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("clinicCode", clinicCode);
        mapSqlParameterSource.addValue("drugCodes", drugCodes);
        mapSqlParameterSource.addValue("batchNumbers", batchNumbers);
        List<InventoryDetail> inventoryDetailListPerClinic = namedParameterJdbcTemplate.query(
                "SELECT inventory.*, charge.Code, drug.TotalQty, drug.TotalAmount, (case when drug.TotalQty != 0 then (drug.TotalAmount / drug.TotalQty) else drug.NettPrice end) as Cost, drugCode.MixingFlag FROM inventorydetailtbl inventory " +
                        "LEFT JOIN clinictbl clinic ON clinic.ClinicID = inventory.ClinicTbl_ID " +
                        "LEFT JOIN codelisting_chargetbl charge ON (charge.CodeListing_ChargeID = inventory.CodeListing_ChargeTbl_ID and charge.Disabled = 0 and charge.DeletedFlag = 0) " +
                        "LEFT JOIN druginventorytbl drug ON (drug.CodeListing_ChargeTbl_ID = inventory.CodeListing_ChargeTbl_ID and drug.ClinicTbl_ID = inventory.ClinicTbl_ID and drug.DeletedFlag = 0) " +
                        "LEFT JOIN codelisting_drugtbl drugCode on (drugCode.CodeListing_ChargeTbl_ID = drug.CodeListing_ChargeTbl_ID and drugCode.DeletedFlag = 0) " +
                        "WHERE clinic.Code = :clinicCode AND charge.Code IN (:drugCodes) " +
                        "AND (inventory.LotNo IN (:batchNumbers) or inventory.LotNo is null) " +
                        "AND (inventory.ExpiryDate >= GETDATE() or inventory.ExpiryDate is null) " +
                        "ORDER BY (case when inventory.LotNo is null then 1 else 0 end), (case when inventory.ExpiryDate is null then 1 else 0 end), inventory.RemainingQty desc, inventory.DateCreated",
                mapSqlParameterSource,
                (rs, rowNum) -> mapRsToInventoryDetail(rs)
        );
        logger.debug("inventoryDetailListPerClinic: " + inventoryDetailListPerClinic);
        for (InventoryUsage inventoryUsage: aggregatedInventoryUsages) {
            List<InventoryDetail> inventoryDetailListPerClinicPerDrug = inventoryDetailListPerClinic.stream()
                    .filter(detail -> detail.getDrugCode().equals(inventoryUsage.getItemCode()))
                    .filter(detail -> detail.getBatchNo() == null || detail.getBatchNo().equals("") || detail.getBatchNo().equals(inventoryUsage.getBatchNo()))
                    .collect(Collectors.toList());
            if (inventoryDetailListPerClinicPerDrug.size() > 0) {
                for (InventoryDetail inventoryDetail: inventoryDetailListPerClinicPerDrug) {
                    logger.debug("inventoryDetail: " + inventoryDetail + ", inventoryUsage: " + inventoryUsage);
                    if (inventoryDetail.getRemainingQuantity() >= inventoryUsage.getQuantity()) {
                        inventoryDetail.setInventoryUsageIndex(inventoryUsage.getIndex());
                        selectedInventoryDetailList.add(inventoryDetail);
                    }
                }
            }
        }
        return selectedInventoryDetailList;
    }

    private int[] updateInventoryDetailList(List<InventoryUsage> inventoryUsageList, Map<Integer, InventoryDetail> inventoryDetailMap) {
        String sql = "UPDATE inventorydetailtbl SET RemainingQty = (CASE WHEN RemainingQty IS NULL THEN 0 - ? ELSE RemainingQty - ? END), RemainingAmount = (CASE WHEN RemainingAmount IS NULL THEN 0 - ? ELSE RemainingAmount - ? END), " +
                "DateUpdated = ?, UserTbl_UpdatedUserID = ?, SyncFlag = ? WHERE InventoryDetailID = ?";
        java.util.Date date = new java.util.Date();
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                InventoryUsage inventoryUsage = inventoryUsageList.get(i);
                InventoryDetail inventoryDetail = inventoryDetailMap.get(inventoryUsage.getIndex());
                ps.setDouble(1, inventoryUsage.getQuantity());
                ps.setDouble(2, inventoryUsage.getQuantity());
                ps.setDouble(3, inventoryDetail.getCost() * inventoryUsage.getQuantity());
                ps.setDouble(4, inventoryDetail.getCost() * inventoryUsage.getQuantity());
                ps.setDate(5, new Date(date.getTime()));
                ps.setInt(6, 1);
                ps.setInt(7, 0);
                ps.setInt(8, inventoryDetail.getInventoryDetailId());
            }

            @Override
            public int getBatchSize() {
                return inventoryDetailMap.size();
            }
        });
    }

    private int[] updateDrugInventoryList(List<InventoryUsage> inventoryUsageList, Map<Integer, InventoryDetail> inventoryDetailMap) {
        String sql = "UPDATE druginventorytbl SET TotalQty = (CASE WHEN TotalQty IS NULL THEN 0 - ? ELSE TotalQty - ? END), TotalAmount = (CASE WHEN TotalAmount IS NULL THEN 0 - ? ELSE TotalAmount - ? END), CurrentTotal = (CASE WHEN CurrentTotal IS NULL THEN 0 - ? ELSE CurrentTotal - ? END), CurrentAmount = (CASE WHEN CurrentAmount IS NULL THEN 0 - ? ELSE CurrentAmount - ? END), DateUpdated = ?, UserTbl_UpdatedUserID = ?, SyncFlag = ? " +
                "WHERE CodeListing_ChargeTbl_ID = ? AND ClinicTbl_ID = ? AND DeletedFlag = ?";
        java.util.Date date = new java.util.Date();
        return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                InventoryUsage inventoryUsage = inventoryUsageList.get(i);
                InventoryDetail inventoryDetail = inventoryDetailMap.get(inventoryUsage.getIndex());
                ps.setDouble(1, inventoryUsage.getQuantity());
                ps.setDouble(2, inventoryUsage.getQuantity());
                ps.setDouble(3, inventoryUsage.getQuantity() * inventoryDetail.getCost());
                ps.setDouble(4, inventoryUsage.getQuantity() * inventoryDetail.getCost());
                ps.setDouble(5, (inventoryDetail.getTotalQuantity() < inventoryUsage.getQuantity()) ? inventoryUsage.getQuantity() : 0);
                ps.setDouble(6, (inventoryDetail.getTotalQuantity() < inventoryUsage.getQuantity()) ? inventoryUsage.getQuantity() : 0);
                ps.setDouble(7, (inventoryDetail.getTotalQuantity() < inventoryUsage.getQuantity()) ? inventoryUsage.getQuantity() * inventoryDetail.getCost() : 0);
                ps.setDouble(8, (inventoryDetail.getTotalQuantity() < inventoryUsage.getQuantity()) ? inventoryUsage.getQuantity() * inventoryDetail.getCost() : 0);
                ps.setDate(9, new Date(date.getTime()));
                ps.setInt(10, 1);
                ps.setInt(11, 0);
                ps.setInt(12, inventoryDetail.getChargeId());
                ps.setInt(13, inventoryDetail.getClinicId());
                ps.setInt(14, 0);
            }

            @Override
            public int getBatchSize() {
                return inventoryDetailMap.size();
            }
        });
    }

    private int getPatientRegisterId(int clinicId) {
        String sql = "SELECT * FROM patientregistertbl WHERE PatientTbl_ID = :patientId and ClinicTbl_ID = :clinicId and DateIn >= :dateIn";
        java.util.Date date = new java.util.Date();
        Date dateIn = new Date(date.getTime());
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("patientId", patientId);
        mapSqlParameterSource.addValue("clinicId", clinicId);
        mapSqlParameterSource.addValue("dateIn", dateIn);
        List<PatientRegister> patientRegisterList = namedParameterJdbcTemplate.query(
            sql,
            mapSqlParameterSource,
            (rs, rowNum) -> mapRsToPatientRegister(rs)
        );
        return patientRegisterList.size() > 0 ? patientRegisterList.get(0).getPatientRegisterId() : 0;
    }

    private int insertPatientRegister(int clinicId) {
        String sql = "INSERT INTO patientregistertbl(PatientTbl_ID, ClinicTbl_ID, DateIn, PatientStatus, " +
            "DateCreated, UserTbl_CreatedUserID, DateUpdated, UserTbl_UpdatedUserID, DatabaseTbl_ID" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String generatedIdKey = "PatientRegisterID";
        java.util.Date date = new java.util.Date();
        Date dateIn = new Date(date.getTime());
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[] { generatedIdKey });
            ps.setInt(1, patientId);
            ps.setInt(2, clinicId);
            ps.setDate(3, dateIn);
            ps.setString(4, "Completed");
            ps.setDate(5, dateIn);
            ps.setInt(6, 1);
            ps.setDate(7, dateIn);
            ps.setInt(8, 1);
            ps.setInt(9, 22);
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    private List<Integer> insertPatientRegisterDetailList(List<InventoryUsage> inventoryUsageList, Map<Integer, InventoryDetail> inventoryDetailMap, int patientRegisterId) {
        String sql = "INSERT INTO patientregisterdetailtbl (" +
                "PatientRegisterTbl_ID, CodeListing_ChargeTbl_ID, LotNo, ExpiryDate, Qty, PaymentSub_Original, PaymentSub_Amended, CostSub_Original, " +
                "DateCreated, UserTbl_CreatedUserID, DateUpdated, UserTbl_UpdatedUserID" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String generatedIdKey = "DrugDispensingID";
        java.util.Date date = new java.util.Date();
        List<Integer> generatedIds = new ArrayList<>();
        for (InventoryUsage inventoryUsage: inventoryUsageList) {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(inventoryUsage.getIndex());
            if (inventoryDetail != null) {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[] { generatedIdKey });
                    ps.setInt(1, patientRegisterId);
                    ps.setInt(2, inventoryDetail.getChargeId());
                    ps.setString(3, inventoryDetail.getBatchNo());
                    ps.setDate(4, inventoryDetail.getExpiryDate());
                    ps.setDouble(5, inventoryUsage.getQuantity());
                    ps.setDouble(6, 0);
                    ps.setDouble(7, 0);
                    ps.setDouble(8, isCostCalculated ? (inventoryDetail.getCost() * inventoryUsage.getQuantity()) : 0);
                    ps.setDate(9, new Date(date.getTime()));
                    ps.setInt(10, 1);
                    ps.setDate(11, new Date(date.getTime()));
                    ps.setInt(12, 1);
                    return ps;
                }, keyHolder);
                int generatedId = keyHolder.getKey().intValue();
                generatedIds.add(generatedId);
                inventoryDetail.setPatientRegisterDetailId(generatedId);
            }
        }
        return generatedIds;
    }

    private List<Integer> insertDrugDispensingList(List<InventoryUsage> inventoryUsageList, Map<Integer, InventoryDetail> inventoryDetailMap) {
        String sql = "INSERT INTO drugdispensingtbl (" +
            "PatientRegisterDetailTbl_ID, CodeListing_ChargeTbl_ID, ClinicTbl_ID, InventoryDetailTbl_ID, Qty, StockAmount, " +
            "DateCreated, UserTbl_CreatedUserID, DateUpdated, UserTbl_UpdatedUserID" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String generatedIdKey = "DrugDispensingID";
        java.util.Date date = new java.util.Date();
        List<Integer> generatedIds = new ArrayList<>();
        for (InventoryUsage inventoryUsage: inventoryUsageList) {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(inventoryUsage.getIndex());
            if (inventoryDetail != null) {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[] { generatedIdKey });
                    ps.setInt(1, inventoryDetail.getPatientRegisterDetailId());
                    ps.setInt(2, inventoryDetail.getChargeId());
                    ps.setInt(3, inventoryDetail.getClinicId());
                    ps.setInt(4, inventoryDetail.getInventoryDetailId());
                    ps.setDouble(5, inventoryUsage.getQuantity());
                    ps.setDouble(6, inventoryDetail.getCost() * inventoryUsage.getQuantity());
                    ps.setDate(7, new Date(date.getTime()));
                    ps.setInt(8, 1);
                    ps.setDate(9, new Date(date.getTime()));
                    ps.setInt(10, 1);
                    return ps;
                }, keyHolder);
                int generatedId = keyHolder.getKey().intValue();
                generatedIds.add(generatedId);
            }
        }
        return generatedIds;
    }

    private List<Integer> insertPatientDrugMixedList(List<InventoryUsage> inventoryUsageList, Map<Integer, InventoryDetail> inventoryDetailMap) {
        String sql = "INSERT INTO patientdrugmixedtbl (" +
                "PatientRegisterDetailTbl_ID, CodeListing_ChargeTbl_ID, Qty, Cost, DateCreated, UserTbl_CreatedUserID, DateUpdated, UserTbl_UpdatedUserID" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String generatedIdKey = "DrugMixedID";
        java.util.Date date = new java.util.Date();
        List<Integer> generatedIds = new ArrayList<>();
        for (InventoryUsage inventoryUsage: inventoryUsageList) {
            InventoryDetail inventoryDetail = inventoryDetailMap.get(inventoryUsage.getIndex());
            if (inventoryDetail != null && inventoryDetail.isMixingFlag()) {
                jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sql, new String[] { generatedIdKey });
                    ps.setInt(1, inventoryDetail.getPatientRegisterDetailId());
                    ps.setInt(2, inventoryDetail.getChargeId());
                    ps.setDouble(3, inventoryUsage.getQuantity());
                    ps.setDouble(4, inventoryDetail.getCost());
                    ps.setDate(5, new Date(date.getTime()));
                    ps.setInt(6, 1);
                    ps.setDate(7, new Date(date.getTime()));
                    ps.setInt(8, 1);
                    return ps;
                }, keyHolder);
                int generatedId = keyHolder.getKey().intValue();
                generatedIds.add(generatedId);
            }
        }
        return generatedIds;
    }

    private List<DrugDispensing> getDrugDispensingList(List<Integer> drugDispensingIds) {
        try {
            if (drugDispensingIds.size() == 0) {
                return new ArrayList<>();
            }
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("drugDispensingIds", drugDispensingIds);

            String sql = "SELECT * FROM drugdispensingtbl WHERE DrugDispensingID IN (:drugDispensingIds)";

            NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
            return namedParameterJdbcTemplate.query(
                sql,
                parameters,
                (rs, rowNum) -> mapRsToDrugDispensing(rs)
            );
        } catch (IncorrectResultSizeDataAccessException exception) {
            return null;
        }
    }

    public List<DrugDispensing> createDrugDispensingList(String clinicCode, List<InventoryUsage> inventoryUsageList) {
        List<InventoryDetail> inventoryDetailList = getInventoryDetailListWithBatchNo(clinicCode, inventoryUsageList);
        logger.debug("Inventory detail list with batch no: " + inventoryDetailList);
        Map<Integer, InventoryDetail> inventoryDetailMap = inventoryDetailList.stream().collect(Collectors.toMap(InventoryDetail::getInventoryUsageIndex, Function.identity()));
        List<InventoryUsage> filteredInventoryUsageList = inventoryUsageList.stream().filter(it -> inventoryDetailMap.keySet().contains(it.getIndex())).collect(Collectors.toList());
        updateInventoryDetailList(filteredInventoryUsageList, inventoryDetailMap);
        updateDrugInventoryList(filteredInventoryUsageList, inventoryDetailMap);

        int clinicId = inventoryDetailMap.size() > 0 ? inventoryDetailMap.get(0).getClinicId() : 0;
        if (clinicId > 0) {
            int patientRegisterIdOfToday = getPatientRegisterId(clinicId);
            if (patientRegisterIdOfToday == 0) {
                patientRegisterIdOfToday = insertPatientRegister(clinicId);
            }
            insertPatientRegisterDetailList(filteredInventoryUsageList, inventoryDetailMap, patientRegisterIdOfToday);
            logger.debug("Patient register detail list inserted for clinic: " + clinicId + ", patient register id: " + patientRegisterIdOfToday);
            insertPatientDrugMixedList(filteredInventoryUsageList, inventoryDetailMap);
        }

        List<Integer> generatedIds = insertDrugDispensingList(filteredInventoryUsageList, inventoryDetailMap);
        return getDrugDispensingList(generatedIds);
    }
}
