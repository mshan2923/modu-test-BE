package com.example.modu.entity.TestElement;
import com.example.modu.dto.result.ResultRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "result")
@NoArgsConstructor
public class Result {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "score", nullable = false)
    private int score;

    @ManyToOne
    @JoinColumn(name = "testerId")
    private Tester tester;

    public Result(ResultRequestDto dto){
        this.image = dto.getImage();
        this.content = dto.getContent();
        this.score = dto.getScore();
    }

    public void setResult(Tester tester){
        this.tester = tester;
    }
}
