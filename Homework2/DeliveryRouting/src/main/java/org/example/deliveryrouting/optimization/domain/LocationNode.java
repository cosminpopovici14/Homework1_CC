package org.example.deliveryrouting.optimization.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.deliveryrouting.common.domain.Coordinates;

@RequiredArgsConstructor
@Getter
@Setter
public class LocationNode {
    private Long id;
    private String name;
    private Coordinates coordinates;
    private int demand;
    private boolean depot;
}
