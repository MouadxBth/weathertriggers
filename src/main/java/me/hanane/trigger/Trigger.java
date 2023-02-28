package me.hanane.trigger;

import lombok.*;
import me.hanane.data.entity.AbstractEntity;
import me.hanane.data.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "triggers")
public class Trigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @NotEmpty
    private String name;
    private String description;
    @Lob
    private String message;
    private String date;
    private boolean runOnce;
    private boolean executed;
    private Integer windDegree;
    private Integer pressure;
    private Integer humidity;
    private Integer visibility;
    private Integer cloudiness;
    private Integer sea_level;
    private Double temperature;
    private Double temp_feels_like;
    private Double minimumTemperature;
    private Double maximumTemperature;
    private Double rainLastHour;
    private Double windSpeed;
    private Double windGust;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @ToString.Exclude
    private User user;

}
