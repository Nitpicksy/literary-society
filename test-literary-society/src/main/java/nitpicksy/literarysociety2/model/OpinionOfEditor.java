package nitpicksy.literarysociety2.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OpinionOfEditor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private User editor;

    @Column(nullable = false, length = 1000)
    private String comment;

    @PrimaryKeyJoinColumn
    @ManyToOne(fetch = FetchType.EAGER)
    private Book book;

    @Column(nullable = false)
    private LocalDateTime created;
}
