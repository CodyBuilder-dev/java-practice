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
    public void setTeam(Team team) { this.team = team; }


}
