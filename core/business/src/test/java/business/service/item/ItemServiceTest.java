package business.service.item;

import business.config.service.SpringTestServiceConfiguration;
import business.mock.MockClinic;
import business.mock.MockItem;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.item.Item;
import com.ilt.cms.core.entity.item.ItemFilter;
import com.ilt.cms.pm.business.service.clinic.inventory.ItemService;
import com.ilt.cms.repository.clinic.ClinicRepository;
import com.ilt.cms.repository.clinic.inventory.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    @Before
    public void setup() throws Exception{
        when(itemRepository.findAll()).thenReturn(
                Arrays.asList(
                        MockItem.mockItem()
                )
        );
        when(itemRepository.findByCode(anyString()))
                .thenAnswer(
                        invocationOnMock -> {
                            String code = invocationOnMock.getArgument(0);
                            Item item = MockItem.mockItem();
                            Field code1 = Item.class.getDeclaredField("code");
                            code1.setAccessible(true);
                            code1.set(item, code);
                            return Optional.of(item);
                        }
                );
        when(itemRepository.findById(anyString()))
                .thenAnswer(
                        invocationOnMock -> {
                            String id = invocationOnMock.getArgument(0);
                            Item item = MockItem.mockItem();
                            Field id1 = PersistedObject.class.getDeclaredField("id");
                            id1.setAccessible(true);
                            id1.set(item, id);
                            return Optional.of(item);
                        }
                );
        when(itemRepository.searchItem(anyString(), anyString(), any(Sort.class))).thenReturn(
                Arrays.asList(
                        MockItem.mockItem()
                )
        );


        when(itemRepository.findAllByItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(anyList(), anyList(), any(Sort.class))).thenReturn(
                Arrays.asList(
                        MockItem.mockItem()
                )
        );
        when(itemRepository.findByCodeAndItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(anyString(), anyList(), anyList(), any(Sort.class)))
                .thenAnswer(
                        invocationOnMock -> {
                            String code = invocationOnMock.getArgument(0);
                            Item item = MockItem.mockItem();
                            Field code1 = Item.class.getDeclaredField("code");
                            code1.setAccessible(true);
                            code1.set(item, code);
                            return Optional.of(item);
                        }
        );
        when(itemRepository.findByIdAndItemFilterClinicIdsInOrItemFilterClinicGroupNamesIn(anyString(), anyList(), anyList(), any(Sort.class)))
                .thenAnswer(
                        invocationOnMock -> {
                            String id = invocationOnMock.getArgument(0);
                            Item item = MockItem.mockItem();
                            Field id1 = PersistedObject.class.getDeclaredField("id");
                            id1.setAccessible(true);
                            id1.set(item, id);
                            return Optional.of(item);
                        }
                );
        when(itemRepository.searchItem(anyString(), anyString(), anyList(), anyList(), any(Sort.class))).thenReturn(
                Arrays.asList(
                        MockItem.mockItem()
                )
        );
        when(clinicRepository.findByIdIn(anyList())).thenReturn(Arrays.asList(MockClinic.mockClinic(), MockClinic.mockClinic()));
    }

    private List<String> mockClinicIds(){
        return Arrays.asList("dijdneur9if8d0dfds", "kd9dhh3bdk8sjdj3");
    }

    private List<String> mockClinicGroupNames(){
        return Arrays.asList("CMS", "LTD");
    }

    @Test
    public void listAllItems(){
        List<Item> items = itemService.listAllItems();
        assertEquals(items.size(), 1);
    }

    @Test
    public void searchItemByCode() throws Exception {
        String code = "DRUG";
        Item item = itemService.searchItemByCode(code);
        assertEquals( code, item.getCode());

    }

    @Test
    public void searchItemById() throws Exception{
        String id = "djhih234h3b3kjbbj";
        Item item = itemService.searchItemById(id);
        assertEquals(id, item.getId());

    }

    @Test
    public void searchItemByRegex() throws Exception {
        String keyword = "drug";
        List<Item> items = itemService.searchItemByRegex(keyword);
        assertEquals(items.size(), 1);
    }

    @Test
    public void listAllItemsWithFilter() throws Exception{
        List<Item> items = itemService.listAllItems(mockClinicIds(), null);
        assertEquals(items.size(), 1);
    }

    @Test
    public void searchItemByCodeWithFilter() throws Exception {
        String code = "DRUG";
        Item item = itemService.searchItemByCode(code, null, mockClinicGroupNames());
        assertEquals( code, item.getCode());

    }

    @Test
    public void searchItemByIdWithFilter() throws Exception{
        String id = "djhih234h3b3kjbbj";
        Item item = itemService.searchItemById(id, mockClinicIds(), mockClinicGroupNames());
        assertEquals(id, item.getId());

    }

    @Test
    public void searchItemByRegexWithFilter() throws Exception {
        String keyword = "drug";
        ItemFilter itemFilter = new ItemFilter();
        List<Item> items = itemService.searchItemByRegex(keyword, mockClinicIds(), null);
        assertEquals(items.size(), 1);
    }
}
