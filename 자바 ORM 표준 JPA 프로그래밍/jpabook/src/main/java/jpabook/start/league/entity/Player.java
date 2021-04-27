package jpabook.start.league.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Player {
    @Id
    private String id;
    private String name;

    @ManyToOne
    private Team team;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Team getTeam() { return team; }
    // 양방향 관계에 안전하게 동작하도록, 연관관계 편의 메서드로 리팩토링
    public void setTeam(Team team) {
        if(this.team != null){
            this.team.getPlayers().remove(this);
        }
        this.team = team;
        team.getPlayers().add(this);
    }


}
