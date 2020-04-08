package ru.javawebinar.topjava.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserService;
import ru.javawebinar.topjava.web.AbstractControllerTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

public class AdminUITest extends AbstractControllerTest {
    private static final String REST_URL = "/ajax/admin/users/";

    @Autowired
    private UserService userService;

    @Test
    void changeStatus() throws Exception {
        User updated = userService.get(USER_ID);
        updated.setEnabled(false);
        perform(MockMvcRequestBuilders.post(REST_URL + USER_ID)
                .param("checkBoxStatus", "false"))
                .andDo(print())
                .andExpect(status().isNoContent());

        USER_MATCHER.assertMatch(userService.get(USER_ID), updated);
    }
}
