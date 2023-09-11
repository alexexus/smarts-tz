package ru.smarts.credit;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreditDto {

    private double limit;
    private double percent;

    @Override
    public String toString() {
        return "Credit{" +
                "limit=" + limit +
                ", percent=" + percent +
                '}';
    }
}
