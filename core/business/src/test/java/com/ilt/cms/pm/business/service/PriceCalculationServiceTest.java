package com.ilt.cms.pm.business.service;

import com.ilt.cms.core.entity.Clinic;
import com.ilt.cms.core.entity.item.*;
import com.ilt.cms.core.entity.item.ClinicItemMaster.ClinicItemPrice;
import com.ilt.cms.core.entity.billing.ItemChargeDetail;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeDetailResponse;
import com.ilt.cms.core.entity.billing.ItemChargeDetail.ItemChargeRequest;
import com.ilt.cms.pm.business.service.clinic.billing.PriceCalculationService;
import com.ilt.cms.pm.business.service.clinic.billing.LegacyInventoryService;
import com.ilt.cms.repository.clinic.*;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import com.lippo.cms.exception.CMSException;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PriceCalculationServiceTest {


    private PriceCalculationService priceCalculationService;
    private ItemRepository itemRepository;
    private ClinicGroupItemMasterRepository clinicGroupItemMasterRepository;
    private ClinicItemMasterRepository clinicItemMasterRepository;
    private ClinicRepository clinicRepository;


    @Before
    public void setup() {

        itemRepository = mock(ItemRepository.class);
        when(itemRepository.findById(anyString())).thenReturn(Optional.of(mockItem()));
        when(itemRepository.existsById(anyString())).thenReturn(true);

        clinicGroupItemMasterRepository = mock(ClinicGroupItemMasterRepository.class);
        when(clinicGroupItemMasterRepository.findGroupPrice(any(), eq("I0002"))).thenReturn(mockClinicGroupItemMaster());

        clinicItemMasterRepository = mock(ClinicItemMasterRepository.class);
        when(clinicItemMasterRepository.findClinicItemPrice(any(), eq("I0003"))).thenReturn(clinicItemMaster());

        clinicRepository = mock(ClinicRepository.class);
        when(clinicRepository.findById(anyString())).thenReturn(Optional.of(new Clinic()));
        priceCalculationService = new PriceCalculationService(
                itemRepository, clinicGroupItemMasterRepository, clinicItemMasterRepository, clinicRepository,
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

    @Test
    public void testPriceCalculator() throws CMSException {
        ItemChargeDetailResponse itemChargeDetailResponse = priceCalculationService.calculateSalesPrice(
                new ItemChargeRequest(null,
                        Arrays.asList(new ItemChargeDetail("I0001", 1, null, null),
                                new ItemChargeDetail("I0002", 2, null, null),
                                new ItemChargeDetail("I0003", 3, null, null),
                                new ItemChargeDetail("I0004", 4, null, null))),
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