package de.cybine.factory.util.api.query;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.List;

@Data
@Jacksonized
@Builder(builderClassName = "Generator")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiConditionInfo
{
    @Builder.Default
    @JsonProperty("method")
    private final EvaluationMethod type = EvaluationMethod.AND;

    @Builder.Default
    @JsonProperty("inverted")
    private final boolean isInverted = false;

    @Valid
    @Singular
    @JsonProperty("details")
    private final List<ApiConditionDetail> details;

    @Valid
    @Singular
    @JsonProperty("sub_conditions")
    private final List<ApiConditionInfo> subConditions;

    @Schema(name = "ApiConditionEvaluationMethod")
    public enum EvaluationMethod
    {
        AND, OR
    }
}
