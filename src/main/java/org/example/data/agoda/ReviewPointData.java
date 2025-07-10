package org.example.data.agoda;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.example.enumData.agoda.ReviewType;

@Data
@Builder
@ToString
public class ReviewPointData {
    ReviewType reviewType;
    Float point;
}
