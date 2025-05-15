package com.foodu.Map.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeocodeResponse {
    private Double latitude;
    private Double longitude;
}