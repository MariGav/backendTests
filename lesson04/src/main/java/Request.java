


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "date",
        "slot",
        "position",
        "type",
        "value"
})
@Data
public class Request {

    @JsonProperty("date")
    public Integer date;
    @JsonProperty("slot")
    public Integer slot;
    @JsonProperty("position")
    public Integer position;
    @JsonProperty("type")
    public String type;
    @JsonProperty("value")
    public RequestValue value;

    }
