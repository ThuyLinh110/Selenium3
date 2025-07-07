package org.example.utils;

import org.example.data.agoda.ReviewPointData;
import org.example.enumData.ReviewType;

import java.util.List;
import java.util.stream.Collectors;

public class ReviewPointUtils {

    public static List<ReviewType> getListReviewTypeFromReviewPointData(List<ReviewPointData> reviewPointData) {
        return reviewPointData.stream()
                .map(ReviewPointData::getReviewType)
                .collect(Collectors.toList());
    }
}
