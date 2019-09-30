package business.service;

import business.config.service.SpringTestServiceConfiguration;
import com.ilt.cms.core.entity.PersistedObject;
import com.ilt.cms.core.entity.notification.Notification;
import com.ilt.cms.pm.business.service.clinic.NotificationService;
import com.ilt.cms.repository.clinic.NotificationRepository;
import com.lippo.cms.exception.CMSException;
import com.lippo.cms.exception.NotificationException;
import com.lippo.commons.util.StatusCode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@Import(SpringTestServiceConfiguration.class)
public class NotificationServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceTest.class);

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationRepository notificationRepository;

    @Before
    public void setUp() throws Exception {

        Notification notification = mockNotification();
        when(notificationRepository.findAllByUsername(anyString())).thenReturn(Arrays.asList(notification));
        when(notificationRepository.findAllByUsernameAndRead(anyString(), anyBoolean())).thenAnswer(
                (Answer<List<Notification>>) invocation -> {
                    String usernameStr = invocation.getArgument(0);
                    boolean isRead = invocation.getArgument(1);
                    Notification notification1 = mockNotification();
                    Field id = Notification.class.getDeclaredField("username");
                    id.setAccessible(true);
                    id.set(notification1, usernameStr);
                    Field read = Notification.class.getDeclaredField("read");
                    read.setAccessible(true);
                    read.set(notification1, isRead);

                    return Arrays.asList(notification1);
                }
        );

        when(notificationRepository.findById(anyString())).thenAnswer(
                (Answer<Optional>) invocation -> {
                    String idStr = invocation.getArgument(0);
                    Notification notification1 = mockNotification();
                    Field id = PersistedObject.class.getDeclaredField("id");
                    id.setAccessible(true);
                    id.set(notification1, idStr);
                    return Optional.of(notification1);
                }
        );
        when(notificationRepository.save(any(Notification.class))).thenAnswer(
                (Answer<Notification>) invocation -> {
                    Notification notification1 = invocation.getArgument(0);
                    if(notification1.getId() == null) {
                        Field id = PersistedObject.class.getDeclaredField("id");
                        id.setAccessible(true);
                        id.set(notification1, "N00001");
                    }
                    return notification1;
                }
        );
    }

    private Notification mockNotification() throws NoSuchFieldException, IllegalAccessException {
        return  mockNotification(null, "username",
                Notification.AddedBy.SYSTEM, "", "This is a system notification", Notification.Priority.INFO);
    }

    private Notification mockNotification(String id, String userName, Notification.AddedBy addedBy, String addId, String message, Notification.Priority priority) throws NoSuchFieldException, IllegalAccessException {
        Notification notification =new Notification(userName, addedBy, addId, message, priority);
        Field fieldId = PersistedObject.class.getDeclaredField("id");
        fieldId.setAccessible(true);
        fieldId.set(notification, id);

        return notification;
    }

    @Test
    public void listNotification() {
        List<Notification> notifications = notificationService.listNotification("Dr Cindy", false);
        assertFalse(notifications.get(0).isRead());

    }

    @Test
    public void listNotificationWithAll() {
        List<Notification> notifications = notificationService.listNotification("Dr Cindy", true);
        assertEquals(1, notifications.size());

    }

    @Test
    public void markNotificationAsReady() throws NotificationException {
        Notification notification = notificationService.markNotificationAsReady("N00009");
        assertEquals("N00009", notification.getId());
    }

    @Test
    public void createNotification() throws NotificationException, NoSuchFieldException, IllegalAccessException {
        Notification notification = notificationService.createNotification(mockNotification());
        assertEquals("N00001", notification.getId());
    }

    @Test
    public void createNotificationWithUserNameNull() throws NotificationException, NoSuchFieldException, IllegalAccessException {
        Notification mockNotification = mockNotification();
        mockNotification.setUsername(null);
        try {
            Notification notification = notificationService.createNotification(mockNotification);

        }catch (CMSException e){
            assertThat(e.getCode(), is(StatusCode.E1002));
        }
    }

    @Test
    public void createNotificationWithMessageEmpty() throws NotificationException, NoSuchFieldException, IllegalAccessException {
        Notification mockNotification = mockNotification();
        mockNotification.setMessage("");
        try {
            Notification notification = notificationService.createNotification(mockNotification);

        }catch (CMSException e){
            assertThat(e.getCode(), is(StatusCode.E1002));
        }

    }

}
