package com.foigen.salary_calculation;

import com.foigen.salary_calculation.controllers.SalaryCalculatorController;
import org.junit.jupiter.api.Assertions;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SalaryCalculationApplicationTests {
    @Autowired
    private SalaryCalculatorController controller;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }

    @Test
    void testCommonCase1() {
        Assertions.assertEquals("{\"status\":\"success\",\"cash\":32850,\"code\":200,\"message\":\"\"}",
                controller.calculate("", 50, 240000).toString());
    }

    @Test
    void testCommonCase2() {
        //в условии сказано, что праздники и выходные должны учитываться при вычислении если известен отрезок,
        //поэтому я убрал начисление за них, когда диапазон не задан, немного не соответствует семантике реальной области, но по тз))
        Assertions.assertEquals("{\"status\":\"success\",\"cash\":22338,\"code\":200,\"message\":\"\"}",
                controller.calculate("20201010", 50, 240000).toString());
    }

    @Test
    void testHolidayCommonCase() {
        //проверка зимних каникул
        Assertions.assertEquals("{\"status\":\"success\",\"cash\":3285,\"code\":200,\"message\":\"\"}",
                controller.calculate("20200101", 14, 240000).toString());
    }

    @Test
    void testCommonParseError() {
        Assertions.assertEquals("{\"status\":\"error\",\"cash\":0,\"code\":500,\"message\":\"Parse error\"}",
                controller.calculate("2020", 14, 240000).toString());
    }

}
