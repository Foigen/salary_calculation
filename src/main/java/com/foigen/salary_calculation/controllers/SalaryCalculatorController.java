package com.foigen.salary_calculation.controllers;

import com.foigen.salary_calculation.responses.BaseResponse;
import com.groupstp.isdayoff.IsDayOff;
import com.groupstp.isdayoff.enums.LocalesType;
import org.joda.time.DateTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
public class SalaryCalculatorController {
    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";
    private static final int CODE_SUCCESS = 200;
    private static final int CODE_SERVER_ERROR = 500;

    //в условии сказано, что праздники и выходные должны учитываться при вычислении если известен отрезок,
    //поэтому я убрал начисление за них, когда диапазон не задан, немного не соответствует семантике реальной области, но по тз))
    @GetMapping("/calculacte")
    public BaseResponse calculate(String start, Integer length, Integer yearlySalary) {
        //по количеству дней и среднегодовой зарплате, не исключает выходные и праздники
        if (start == null || start.equals("")) {
            return new BaseResponse(SUCCESS_STATUS, yearlySalary / 365 * length, CODE_SUCCESS, "");
        }
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        //исключает выходные и праздники
        try {
            var startDate = df.parse(start);
            var end = new DateTime(startDate).plusDays(length).toDate();

            System.out.println("------");
            System.out.println("start = " + startDate);
            System.out.println("end = " + end);

            IsDayOff ido = IsDayOff.Builder().setLocale(LocalesType.RUSSIA).build();
            var list = ido.daysTypeByRange(startDate, end);

            System.out.println("list size" + list.size());

            var count = Math.toIntExact(list.stream().filter(x -> x.getDayType().isWorkingDay()).count());

            System.out.println("count = " + count);

            return new BaseResponse(SUCCESS_STATUS, yearlySalary / 365 * count, CODE_SUCCESS, "");
        } catch (ParseException e) {
            return new BaseResponse(ERROR_STATUS, 0, CODE_SERVER_ERROR, "Parse error");
        } catch (Exception e) {
            return new BaseResponse(ERROR_STATUS, 0, CODE_SERVER_ERROR, "Unknown error");
        }
    }

}
