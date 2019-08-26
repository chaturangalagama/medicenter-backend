package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.Status;
import com.ilt.cms.core.entity.charge.Charge;
import com.ilt.cms.core.entity.coverage.CoveragePlan;
import com.ilt.cms.core.entity.coverage.MedicalCoverage;
import com.ilt.cms.core.entity.item.*;
import com.ilt.cms.core.entity.item.ClinicItemMaster.ClinicItemPrice;
import com.ilt.cms.core.entity.visit.AttachedMedicalCoverage;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeDetailResponse;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.pm.business.service.inventory.LegacyInventoryService;
import com.ilt.cms.repository.spring.*;
import com.ilt.cms.repository.spring.coverage.MedicalCoverageRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceCalculationServiceTest {


    private PriceCalculationService priceCalculationService;
    private MedicalCoverageRepository medicalCoverageRepository;
    private MedicalCoverageItemRepository medicalCoverageItemRepository;
    private ItemRepository itemRepository;
    private ClinicGroupItemMasterRepository clinicGroupItemMasterRepository;
    private ClinicItemMasterRepository clinicItemMasterRepository;
    private ClinicRepository clinicRepository;


    @Before
    public void setup() {
        medicalCoverageRepository = mock(MedicalCoverageRepository.class);
        when(medicalCoverageRepository.findMedicalCoverageByPlanId(anyString(), eq(Status.ACTIVE)))
                .thenReturn(Arrays.asList(mockMedicalCoverage()));

        medicalCoverageItemRepository = mock(MedicalCoverageItemRepository.class);
        when(medicalCoverageItemRepository.findByPlanAndItemId(any(), eq("I0001")))
                .thenReturn(new MedicalCoverageItem("PL0001",
                        Arrays.asList(new ItemCoverageScheme("I0001",
                                new Charge(100, false))), Collections.emptyList()));

        itemRepository = mock(ItemRepository.class);
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(mockItem()));
        when(itemRepository.existsById(anyString())).thenReturn(true);

        clinicGroupItemMasterRepository = mock(ClinicGroupItemMasterRepository.class);
        when(clinicGroupItemMasterRepository.findGroupPrice(any(), eq("I0002"))).thenReturn(mockClinicGroupItemMaster());

        clinicItemMasterRepository = mock(ClinicItemMasterRepository.class);
        when(clinicItemMasterRepository.findClinicItemPrice(any(), eq("I0003"))).thenReturn(clinicItemMaster());

        clinicRepository = mock(ClinicRepository.class);
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(new Clinic()));
        priceCalculationService = new PriceCalculationService(medicalCoverageRepository, medicalCoverageItemRepository,
                itemRepository, clinicGroupItemMasterRepository, clinicItemMasterRepository, clinicRepository, mock(CaseRepository.class),
                mock(LegacyInventoryService.class));
    }

    private ClinicItemMaster clinicItemMaster() {
        ClinicItemMaster clinicItemMaster = new ClinicItemMaster("CL0001", Arrays.asList(new ClinicItemPrice("I0003", 300)));
        return clinicItemMaster;
    }

    private ClinicGroupItemMaster mockClinicGroupItemMaster() {
        ClinicGroupItemMaster clinicGroupItemMaster = new ClinicGroupItemMaster();
        clinicGroupItemMaster.setClinicGroupItemPrices(Arrays.asList(new ClinicGroupItemMaster.ClinicGroupItemPrice("I0002", 200)));
        return clinicGroupItemMaster;
    }

    private Item mockItem() {
        Item item = new Item();
        item.setSellingPrice(new SellingPrice(400, false));
        return item;
    }

    private MedicalCoverage mockMedicalCoverage() {
        MedicalCoverage medicalCoverage = new MedicalCoverage();
        medicalCoverage.setType(MedicalCoverage.CoverageType.CORPORATE);
        medicalCoverage.setCoveragePlans(Arrays.asList(new CoveragePlan()));
        return medicalCoverage;
    }

    @Test
    public void testPriceCalculator() throws CMSException {
        ItemChargeDetailResponse itemChargeDetailResponse = priceCalculationService.calculateSalesPrice(Arrays.asList(new AttachedMedicalCoverage("PL0001")),
                new ItemChargeRequest(null,
                        Arrays.asList(new ItemChargeDetail("I0001", 1, null, null, Collections.emptySet()),
                                new ItemChargeDetail("I0002", 2, null, null, Collections.emptySet()),
                                new ItemChargeDetail("I0003", 3, null, null, Collections.emptySet()),
                                new ItemChargeDetail("I0004", 4, null, null, Collections.emptySet()))),
                "CL0001");
        for (ItemChargeDetail chargeDetail : itemChargeDetailResponse.getChargeDetails()) {
            if (chargeDetail.getItemId().equals("I0001")) {
                assertEquals(100, chargeDetail.getCharge().getPrice());
            } else if (chargeDetail.getItemId().equals("I0002")) {
                assertEquals(200, chargeDetail.getCharge().getPrice());
            } else if (chargeDetail.getItemId().equals("I0003")) {
                assertEquals(300, chargeDetail.getCharge().getPrice());
            } else if (chargeDetail.getItemId().equals("I0004")) {
                assertEquals(400, chargeDetail.getCharge().getPrice());
            } else {
                throw new RuntimeException("Item ID not valid [" + chargeDetail.getItemId() + "]");
            }
        }
    }
}