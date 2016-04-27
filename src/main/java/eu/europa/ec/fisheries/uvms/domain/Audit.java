package eu.europa.ec.fisheries.uvms.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Embeddable
@EqualsAndHashCode
@ToString
public class Audit implements Serializable {

    public static final String CREATED_ON = "created_on";

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = CREATED_ON, nullable = false)
    private Date createdOn;

    Audit() {
    }

    public Audit(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }
}
