package facades;

import dtos.TripDTO;
import entities.Person;
import entities.Trip;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class TripFacade {

    private static EntityManagerFactory emf;
    private static TripFacade instance;

    private TripFacade() {
    }


    public static TripFacade getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripFacade();
        }
        return instance;
    }

    public List<TripDTO> getAllTrips() {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t", Trip.class);
            List<Trip> allTrips = query.getResultList();
            return TripDTO.getDTOs(allTrips);
        } finally {
            em.close();
        }
    }

    public TripDTO createAndAddPersonToTrip(Person person, Trip trip) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(person);
            trip.getPeople().add(person);
            em.merge(trip);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new TripDTO(trip);
    }


    public Trip getTripById(Integer id) {
        EntityManager em = emf.createEntityManager();
        Trip trip;
        try {
            em.getTransaction().begin();
            trip = em.find(Trip.class, id);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return trip;
    }

    //public Person createPerson(Person person) {
//
  //  }
}