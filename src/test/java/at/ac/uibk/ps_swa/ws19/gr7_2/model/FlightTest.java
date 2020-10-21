package at.ac.uibk.ps_swa.ws19.gr7_2.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FlightTest {

    private static Set<User> pilots;
    private static Set<User> crewMembers;

    private static int amountOfPilots = 3;
    private static int amountOfCrewMembers = 3;

    @BeforeAll
    static void setupUsers() {
        setupPilots();
        setupCrew();
    }

    private static void setupPilots() {

        HashSet<UserRole> pilotRoles = new HashSet<>();
        pilotRoles.add(UserRole.PILOT);
        pilots = new HashSet<>();
        for(int i = 0; i < amountOfPilots; i++) {
            User u = new User();
            u.setRoles(new HashSet<>(pilotRoles));
            u.setFirstName(i + ".");
            u.setLastName("Pilot");
            u.setUsername("pilot" + i);

            pilots.add(u);
        }
    }

    private static void setupCrew() {

        HashSet<UserRole> crewRoles = new HashSet<>();
        crewRoles.add(UserRole.CREW);
        crewMembers = new HashSet<>();
        for(int i = 0; i < amountOfCrewMembers; i++) {
            User u = new User();
            u.setRoles(new HashSet<>(crewRoles));
            u.setFirstName(i + ".");
            u.setLastName("Crew");
            u.setUsername("crew" + i);

            crewMembers.add(u);
        }
    }

    @Test
    void testGetCrewAndPilots() {

        Flight flight = new Flight();
        flight.setPilots(pilots);
        flight.setCrew(crewMembers);

        int i = 0;
        // Check if all users in crewAndPilots is contained in either pilots or crewMembers
        for (Iterator<User> it = flight.getCrewAndPilots().iterator(); it.hasNext(); ) {
            User u = it.next();

            assertTrue(pilots.contains(u) || crewMembers.contains(u));
            i++;
        }
        // Check if crewAndPilots doesn't contain more users than it should
        assertEquals(pilots.size() + crewMembers.size(), i);
    }

    @Test
    void testGetCrewAndPilotsNull() {

        Flight flight = new Flight();
        flight.setPilots(null);
        flight.setCrew(null);
        // crew and pilots are null in this flight object. See if the stream getCrewAndPilots() is empty
        assertEquals(0, flight.getCrewAndPilots().count());
    }

    @Test
    void testGetCrewAndPilotsPartiallyNull() {

        // Test with crew = null
        Flight flight1 = new Flight();
        flight1.setPilots(pilots);

        int i = 0;
        for (Iterator<User> it = flight1.getCrewAndPilots().iterator(); it.hasNext(); ) {
            User u = it.next();

            assertTrue(pilots.contains(u));
            i++;
        }
        assertEquals(amountOfPilots, i);

        // Test with pilots = null
        Flight flight2 = new Flight();
        flight2.setCrew(crewMembers);

        i = 0;
        for (Iterator<User> it = flight2.getCrewAndPilots().iterator(); it.hasNext(); ) {
            User u = it.next();

            assertTrue(crewMembers.contains(u));
            i++;
        }
        assertEquals(amountOfCrewMembers, i);
    }

    @Test
    void testGetFlightPersonnelAmount() {

        Flight flight = new Flight();
        flight.setCrew(crewMembers);
        flight.setPilots(pilots);
        assertEquals(amountOfCrewMembers + amountOfPilots, flight.getFlightPersonnelAmount());
    }

    @Test
    void testGetFlightPersonnelAmountNull() {

        Flight flight = new Flight();
        assertEquals(0, flight.getFlightPersonnelAmount());
    }

    @Test
    void testGetFlightPersonnelAmountPartiallyNull() {

        // Test with crew = null
        Flight flight1 = new Flight();
        flight1.setPilots(pilots);
        assertEquals(amountOfPilots, flight1.getFlightPersonnelAmount());

        // Test with pilots = null
        Flight flight2 = new Flight();
        flight2.setCrew(crewMembers);
        assertEquals(amountOfCrewMembers, flight2.getFlightPersonnelAmount());
    }
}