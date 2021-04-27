package jpabook.start.league.entity;

import jpabook.start.shop.entity.Member;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Team {
    @Id
    private String id;
    private String name;

    //@OneToMany() // mappedBy를 빼먹지 말자!
    @OneToMany(mappedBy = "team")
    private List<Player> players;

    public List<Player> getPlayers() { return players; }
    public void setPlayers(List<Player> players) { this.players = players; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
