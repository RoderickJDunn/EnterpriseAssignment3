package dataaccess;

import bouncingsprites.Sprite;
import bouncingsprites.SpriteSimulation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.hibernate.service.ServiceRegistry;
import org.junit.BeforeClass;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Roderick on 2016-10-26.
 */
public enum DataAccess {
    // TODO: refactor to singleton
    INSTANCE;

    private SessionFactory factory;
    private ServiceRegistry sR;

    DataAccess() {
        Configuration config = new Configuration()
                .addAnnotatedClass(Sprite.class)
                .configure("hibernate.cfg.xml");

        StandardServiceRegistryBuilder sRBuilder =
                new StandardServiceRegistryBuilder().applySettings(config.getProperties());
        sR = sRBuilder.build();
        factory = config.buildSessionFactory(sR);  // we create a factory only once
    }

    public static DataAccess getInstance() { return INSTANCE; }

    public List<Sprite> getPersistedSprites() {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        List results = session.createQuery( "FROM Sprite").list();
        session.close();
        return results; // TODO: this is for a test... change it to results.
    }

    public void saveSprites(List<Sprite> sprites) {
        Session session = factory.getCurrentSession();
        session.beginTransaction();
        for (int i=0; i<sprites.size(); i++) {
            session.save(sprites.get(i));
        }
        session.getTransaction().commit();
        session.close();
    }

    public Session getSession() {
        // TODO: close open session before returning new one?
        return factory.getCurrentSession();
    }

    /**
     * To be called on program shutdown
     */
    public void terminate() {
        factory.close();
        StandardServiceRegistryBuilder.destroy(sR);
    }
}
