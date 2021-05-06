package jpabook.start;

import jpabook.start.shop.entity.Album;
import jpabook.start.shop.entity.Movie;
import org.junit.Assert;
import org.junit.Test;

public class EntityGenerationTest {

    @Test
    public void InheritanceTest(){
        Assert.assertNotNull(Album.class);
        Album a = new Album();
        Assert.assertNotNull(a);

        Assert.assertNotNull(Movie.class);
        Movie m = new Movie();
        Assert.assertNotNull(m);
    }
}
