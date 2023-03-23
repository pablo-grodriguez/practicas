package es.udc.paproject.backend.rest.dtos;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class BillboardParamsDto {

    private int day;

    @NotNull
    @Range(min = 0,max = 6)
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
