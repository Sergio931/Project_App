package com.itmo.client;

import com.itmo.app.Location;
import com.itmo.app.Person;
import com.itmo.app.StudyGroup;
import com.itmo.utils.DateTimeAdapter;
import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
public class StudyGroupForUITable implements Serializable {
    private Long id;
    private String name;
    private String creationDate;
    private ZonedDateTime creationDateForParsing;
    private Long x;
    private Long y;
    private Long studentsCount;
    private String formOfEducation;
    private String semester;
    private String adminName;
    private Long height;
    private Long weight;
    private String passportID;
    private Double locationX;
    private Long locationY;
    private String locationName;
    private String owner;
    private double red;
    private double green;
    private double blue;
    private DateFormat format;

    public StudyGroupForUITable(StudyGroup studyGroup, DateFormat format) {
        this.format = format;
        Person person = studyGroup.getGroupAdmin();
        Location location = person.getLocation();
        id = studyGroup.getId();
        name = studyGroup.getName();
        x = studyGroup.getCoordinates().getX();
        y = studyGroup.getCoordinates().getY();
        creationDate = DateTimeAdapter.parseToString(studyGroup.getCreationDate(), format);
        creationDateForParsing = studyGroup.getCreationDate();
        studentsCount = studyGroup.getStudentsCount();
        formOfEducation = studyGroup.getFormOfEducation().getEnglish();
        semester = studyGroup.getSemesterEnum().getEnglish();
        adminName = person.getName();
        height = person.getHeight();
        weight = person.getWeight();
        passportID = person.getPassportID();
        locationX = location.getX();
        locationY = location.getY();
        locationName = location.getName();
        owner = studyGroup.getOwner().getName();
        Color color = studyGroup.getOwner().getColor();
        red = color.getRed();
        green = color.getGreen();
        blue = color.getBlue();
    }

    public void changeDateFormat(DateFormat dateFormat) {
        this.format = dateFormat;
        creationDate = DateTimeAdapter.parseToString(creationDateForParsing, format);
    }
}
