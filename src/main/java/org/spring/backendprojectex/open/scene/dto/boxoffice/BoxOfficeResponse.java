package org.spring.backendprojectex.open.scene.dto.boxoffice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoxOfficeResponse {
    private BoxOfficeResult boxOfficeResult;
}
