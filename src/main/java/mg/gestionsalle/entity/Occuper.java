package mg.gestionsalle.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "occuper")
public class Occuper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "codeprof")
    private Prof prof;

    @ManyToOne
    @JoinColumn(name = "codesal")
    private Salle salle;

    @Column(name = "date_occupation")
    private LocalDate dateOccupation;

    public Occuper() {
    }

    public Occuper(Prof prof, Salle salle, LocalDate dateOccupation) {
        this.prof = prof;
        this.salle = salle;
        this.dateOccupation = dateOccupation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Prof getProf() {
        return prof;
    }

    public void setProf(Prof prof) {
        this.prof = prof;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public LocalDate getDateOccupation() {
        return dateOccupation;
    }

    public void setDateOccupation(LocalDate dateOccupation) {
        this.dateOccupation = dateOccupation;
    }
}