package it.cedacri.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.cedacri.utils.MyCustomDeserializer;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.HashMap;
import java.util.Map;
@Schema(name = "User", description = "User Representation")
public class User {

    @Schema(required = true)
    private Long id;
    @Schema(required = true)
    private String fullName;
    @Schema(required = true)
    @JsonDeserialize(keyUsing = MyCustomDeserializer.class)
    private Map<String, Integer> ratings = new HashMap<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Map<String, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<String, Integer> ratings) {
        this.ratings = ratings;
    }
}
